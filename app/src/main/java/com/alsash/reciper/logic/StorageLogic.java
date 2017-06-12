package com.alsash.reciper.logic;


import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.CategoryTable;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.LabelTable;
import com.alsash.reciper.data.db.table.RecipeTable;
import com.alsash.reciper.logic.exception.MainThreadException;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Logic for processing data in storage.
 * All methods, except constructor, must be called on the background thread.
 */
public class StorageLogic {

    private static final String TAG = "StorageLogic";

    private static final long CLOUD_DB_CHECK_INTERVAL_MS
            = AppContract.Cloud.Github.Db.CHECK_INTERVAL_MS;

    private final CloudManager cloudManager;
    private final DbManager dbManager;

    public StorageLogic(DbManager dbManager, CloudManager cloudManager) {
        this.dbManager = dbManager;
        this.cloudManager = cloudManager;
    }

    /**
     * Fetch startup entities from cloud and create it in database.
     * Must be called on the background thread.
     */
    public void createStartupEntitiesIfNeed() throws Exception {
        MainThreadException.throwOnMainThread(TAG, "createStartupEntitiesIfNeed()");
        if (isDbUpToDate()) return;
        Date createdAt = new Date();
        if (createStartupEntities(createdAt)) {
            dbManager.setSettingsUpdateDate(createdAt);
        } else {
            deleteAllEntitiesCreatedAt(createdAt);
        }
    }

    public List<Category> getCategories(int offset, int limit) {
        List<Category> categories = new ArrayList<>();
        List<CategoryTable> categoryTables = dbManager
                .restrictWith(offset, limit)
                .getCategoryTable();
        for (CategoryTable categoryTable : categoryTables) {
            // Prefetch to-one relations
            categoryTable.getPhoto();
            categories.add(categoryTable);
        }
        return categories;
    }

    public List<Label> getLabels(int offset, int limit) {
        List<Label> labels = new ArrayList<>();
        List<LabelTable> labelTables = dbManager
                .restrictWith(offset, limit)
                .getLabelTable();
        labels.addAll(labelTables);
        return labels;
    }

    public List<Recipe> getRecipes(int offset, int limit) {
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable();
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public List<Recipe> getRecipes(Category category, int offset, int limit) {
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((CategoryTable) category);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public List<Recipe> getRecipes(Label label, int offset, int limit) {
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((LabelTable) label);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    private boolean isDbUpToDate() {
        Date localUpdateDate = dbManager.getSettingsUpdateDate();
        // First access
        if (localUpdateDate == null) return false;

        // Next access
        // Check interval
        long checkInterval = new Date().getTime() - localUpdateDate.getTime();
        if (checkInterval <= CLOUD_DB_CHECK_INTERVAL_MS) return true;

        // Check updates
        Date cloudUpdateDate = cloudManager.getDbUpdateDate();
        if (cloudUpdateDate == null) return true;

        return cloudUpdateDate.getTime() <= localUpdateDate.getTime();
    }

    private void deleteAllEntitiesCreatedAt(Date createdAt) {
        dbManager.changeWith(createdAt).deleteAll();
    }

    private boolean createStartupEntities(Date createdAt) {
        if (!cloudManager.isOnline()) return false;
        String dbLanguage = cloudManager.getDbLanguage(Locale.getDefault());
        if (dbLanguage == null) dbLanguage = cloudManager.getDbLanguage(Locale.ENGLISH);
        if (dbLanguage == null) return false;
        boolean modify = dbManager
                .changeWith(createdAt)
                .modifyAll(
                        cloudManager.getDbAuthorTable(dbLanguage),
                        cloudManager.getDbCategoryTable(dbLanguage),
                        cloudManager.getDbFoodMeasureTable(dbLanguage),
                        cloudManager.getDbFoodTable(dbLanguage),
                        cloudManager.getDbFoodUsdaTable(dbLanguage),
                        cloudManager.getDbLabelTable(dbLanguage),
                        cloudManager.getDbPhotoTable(dbLanguage),
                        cloudManager.getDbRecipeFoodTable(dbLanguage),
                        cloudManager.getDbRecipeLabelTable(dbLanguage),
                        cloudManager.getDbRecipeMethodTable(dbLanguage),
                        cloudManager.getDbRecipePhotoTable(dbLanguage),
                        cloudManager.getDbRecipeTable(dbLanguage)
                );
        boolean update = updateFoodUsda(createdAt);
        return modify && update;
    }

    private boolean updateFoodUsda(Date updateDate) {
        List<FoodTable> dbFoods;
        if (!cloudManager.isOnline()) return false; // not updated
        do {
            // Loading foods, up to 20 items, without updated nutrition values
            dbFoods = dbManager.restrictWith(0, 20).getFoodTable(false);
            if (dbFoods.size() == 0) break;

            // Fetching nutrition values
            String[] ndbNos = new String[dbFoods.size()];
            for (int i = 0; i < dbFoods.size(); i++) {
                ndbNos[i] = dbFoods.get(i).getFoodUsdaTables().get(0).getNdbNo();
            }
            Map<String, FoodTable> usdaResult = cloudManager.getUsdaFoodTable(ndbNos);
            if (usdaResult.size() == 0) return false; // not updated

            // Updating nutrition values
            for (Map.Entry<String, FoodTable> entry : usdaResult.entrySet()) {
                boolean found = false;
                for (FoodTable dbFood : dbFoods) {
                    if (dbFood.getFoodUsdaTables().get(0).getNdbNo().equals(entry.getKey())) {
                        FoodTable usdaFood = entry.getValue();
                        if (dbFood.getName() == null) dbFood.setName(usdaFood.getName());
                        dbFood.setProtein(usdaFood.getProtein());
                        dbFood.setFat(usdaFood.getFat());
                        dbFood.setCarbs(usdaFood.getCarbs());
                        dbFood.setWeightUnit(usdaFood.getWeightUnit());
                        dbFood.setEnergy(usdaFood.getEnergy());
                        dbFood.setEnergyUnit(usdaFood.getEnergyUnit());
                        dbFood.getFoodUsdaTables().get(0).setFetched(true);
                        found = true;
                        break;
                    }
                }
                if (!found) return false; // not updated
            }
            dbManager.changeWith(updateDate).modifyDeepFoodTable(dbFoods);
        } while (dbFoods.size() > 0);
        return true;
    }

    private List<RecipeTable> prefetchRecipeRelations(List<RecipeTable> recipeTables) {
        for (RecipeTable recipeTable : recipeTables) prefetchRecipeRelations(recipeTable);
        return recipeTables;
    }

    private List<RecipeTable> prefetchRecipeRelationsFull(List<RecipeTable> recipeTables) {
        for (RecipeTable recipeTable : recipeTables) prefetchRecipeRelationsFull(recipeTable);
        return recipeTables;
    }

    private RecipeTable prefetchRecipeRelations(RecipeTable recipeTable) {
        recipeTable.getAuthor();
        recipeTable.getCategory();
        recipeTable.getMainPhoto();
        return recipeTable;
    }

    private RecipeTable prefetchRecipeRelationsFull(RecipeTable recipeTable) {
        prefetchRecipeRelations(recipeTable);
        recipeTable.getPhotos();
        recipeTable.getLabels();
        recipeTable.getIngredients();
        recipeTable.getMethods();
        return recipeTable;
    }
}
