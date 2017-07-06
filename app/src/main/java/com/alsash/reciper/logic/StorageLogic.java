package com.alsash.reciper.logic;


import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.alsash.reciper.BuildConfig;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.AuthorTable;
import com.alsash.reciper.data.db.table.CategoryTable;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.FoodUsdaTable;
import com.alsash.reciper.data.db.table.LabelTable;
import com.alsash.reciper.data.db.table.PhotoTable;
import com.alsash.reciper.data.db.table.RecipeFoodTable;
import com.alsash.reciper.data.db.table.RecipeLabelTable;
import com.alsash.reciper.data.db.table.RecipeMethodTable;
import com.alsash.reciper.data.db.table.RecipePhotoTable;
import com.alsash.reciper.data.db.table.RecipeTable;
import com.alsash.reciper.data.db.table.Table;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.exception.MainThreadException;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.logic.unit.EnergyUnit;
import com.alsash.reciper.logic.unit.WeightUnit;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Measure;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * A Storage Logic for processing data in storage.
 * All methods, except explicit annotated, must be called on the background thread.
 */
public class StorageLogic {

    private static final String TAG = "StorageLogic";

    private static final long CLOUD_DB_CHECK_INTERVAL_MS
            = AppContract.Cloud.Github.Db.CHECK_INTERVAL_MS;

    private final CloudManager cloudManager;
    private final DbManager dbManager;

    private final Comparator<RecipeMethodTable> methodComparator
            = new Comparator<RecipeMethodTable>() {
        @Override
        public int compare(RecipeMethodTable o1, RecipeMethodTable o2) {
            return (o1.getIndex() < o2.getIndex()) ? -1 :
                    ((o1.getIndex() == o2.getIndex()) ? 0 : 1);
        }
    };

    @UiThread
    public StorageLogic(DbManager dbManager, CloudManager cloudManager) {
        this.dbManager = dbManager;
        this.cloudManager = cloudManager;
    }

    /**
     * Fetch startup entities from cloud and create it in database.
     * Must be called on the background thread.
     */
    public void createStartupEntitiesIfNeed() throws Exception {
        if (BuildConfig.DEBUG)
            MainThreadException.throwOnMainThread(TAG, "createStartupEntitiesIfNeed()");
        Date createdAt = new Date();
        if (isDbUpToDate()) {
            updateFoodUsda(createdAt);
        } else if (createStartupEntities(createdAt)) {
            dbManager.setSettingsUpdateDate(createdAt);
            updateFoodUsda(createdAt);
        } else {
            deleteAllEntitiesCreatedAt(createdAt);
        }
    }

    public int getRelatedRecipesSize(BaseEntity entity) {
        if (!hasRelatedRecipes(entity)) return 0;
        if (entity instanceof CategoryTable) {
            CategoryTable categoryTable = (CategoryTable) entity;
            return dbManager.getRecipeTable(categoryTable).size(); // Distinct count.
        } else if (entity instanceof LabelTable) {
            LabelTable labelTable = (LabelTable) entity;
            return dbManager.getRecipeTable(labelTable).size();
        } else if (entity instanceof FoodTable) {
            FoodTable foodTable = (FoodTable) entity;
            return dbManager.getRecipeTable(foodTable).size();
        } else if (entity instanceof AuthorTable) {
            AuthorTable authorTable = (AuthorTable) entity;
            return dbManager.getRecipeTable(authorTable).size();
        }
        return 0;
    }

    public boolean hasRelatedRecipes(BaseEntity entity) {
        if (entity instanceof CategoryTable) {
            CategoryTable categoryTable = (CategoryTable) entity;
            return dbManager.restrictWith(0, 1).getRecipeTable(categoryTable).size() > 0;
        } else if (entity instanceof LabelTable) {
            LabelTable labelTable = (LabelTable) entity;
            return dbManager.restrictWith(0, 1).getRecipeTable(labelTable).size() > 0;
        } else if (entity instanceof FoodTable) {
            FoodTable foodTable = (FoodTable) entity;
            return dbManager.restrictWith(0, 1).getRecipeTable(foodTable).size() > 0;
        } else if (entity instanceof AuthorTable) {
            AuthorTable authorTable = (AuthorTable) entity;
            return dbManager.restrictWith(0, 1).getRecipeTable(authorTable).size() > 0;
        }
        return false;
    }

    public List<Category> getCategories(int offset, int limit) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "getCategories()");
        List<Category> categories = new ArrayList<>();
        List<CategoryTable> categoryTables = dbManager
                .restrictWith(offset, limit)
                .getCategoryTable();
        for (CategoryTable categoryTable : categoryTables) {
            categories.add(prefetchRelation(categoryTable));
        }
        return categories;
    }

    public List<Label> getLabels(int offset, int limit) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "getLabels()");
        List<Label> labels = new ArrayList<>();
        List<LabelTable> labelTables = dbManager
                .restrictWith(offset, limit)
                .getLabelTable();
        labels.addAll(labelTables);
        return labels;
    }

    public List<Food> getFoods(int offset, int limit) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "getFoods()");
        List<Food> foods = new ArrayList<>();
        List<FoodTable> foodTables = dbManager
                .restrictWith(offset, limit)
                .getFoodTable();
        prefetchFoodRelations(foodTables);
        foods.addAll(foodTables);
        return foods;
    }

    public List<Author> getAuthors(int offset, int limit) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "getAuthors()");
        List<Author> authors = new ArrayList<>();
        List<AuthorTable> authorTables = dbManager
                .restrictWith(offset, limit)
                .getAuthorTable();
        prefetchAuthorRelations(authorTables);
        authors.addAll(authorTables);
        return authors;
    }

    public List<Recipe> getRecipes(int offset, int limit) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "getRecipes(int, int)");
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable();
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public List<Recipe> getRecipes(Category category, int offset, int limit) {
        if (BuildConfig.DEBUG)
            MainThreadException.throwOnMainThread(TAG, "getRecipes(Category, int, int)");
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((CategoryTable) category);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public List<Recipe> getRecipes(Label label, int offset, int limit) {
        if (BuildConfig.DEBUG)
            MainThreadException.throwOnMainThread(TAG, "getRecipes(Label, int, int)");
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((LabelTable) label);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public List<Recipe> getRecipes(Food food, int offset, int limit) {
        if (BuildConfig.DEBUG)
            MainThreadException.throwOnMainThread(TAG, "getRecipes(Food, int, int)");
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((FoodTable) food);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public List<Recipe> getRecipes(Author author, int offset, int limit) {
        if (BuildConfig.DEBUG)
            MainThreadException.throwOnMainThread(TAG, "getRecipes(Author, int, int)");
        List<Recipe> recipes = new ArrayList<>();
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((AuthorTable) author);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public BaseEntity getRestrictEntity(EntityRestriction restriction) {

        if (BuildConfig.DEBUG)
            MainThreadException.throwOnMainThread(TAG, "getRestrictEntity(EntityRestriction)");

        if (restriction.entityClass.equals(Category.class)) {

            return prefetchRelation(dbManager.getCategoryTable(restriction.entityUuid));

        } else if (restriction.entityClass.equals(Label.class)) {

            return dbManager.getLabelTable(restriction.entityUuid);

        } else if (restriction.entityClass.equals(Food.class)) {

            return dbManager.getFoodTable(restriction.entityUuid);

        } else if (restriction.entityClass.equals(Author.class)) {

            return dbManager.getAuthorTable(restriction.entityUuid);

        } else if (restriction.entityClass.equals(Recipe.class)) {

            return prefetchRelation(dbManager.getRecipeTable(restriction.entityUuid));

        } else if (restriction.entityClass.equals(RecipeFull.class)) {

            return prefetchRelationFull(dbManager.getRecipeTable(restriction.entityUuid));

        } else {
            return null;
        }
    }

    public List<Recipe> getRestrictRecipes(EntityRestriction restriction, int offset, int limit) {
        List<Recipe> recipes = new ArrayList<>();
        if (restriction == null
                || restriction.entityUuid == null
                || restriction.entityClass == null)
            return recipes;
        if (restriction.entityClass.equals(Category.class)) {

            CategoryTable categoryTable = new CategoryTable();
            categoryTable.setUuid(restriction.entityUuid);
            return getRecipes(categoryTable, offset, limit);

        } else if (restriction.entityClass.equals(Label.class)) {

            LabelTable labelTable = new LabelTable();
            labelTable.setUuid(restriction.entityUuid);
            return getRecipes(labelTable, offset, limit);

        } else if (restriction.entityClass.equals(Food.class)) {

            FoodTable foodTable = new FoodTable();
            foodTable.setUuid(restriction.entityUuid);
            return getRecipes(foodTable, offset, limit);

        } else if (restriction.entityClass.equals(Author.class)) {

            AuthorTable authorTable = new AuthorTable();
            authorTable.setUuid(restriction.entityUuid);
            return getRecipes(authorTable, offset, limit);

        } else {
            return recipes;
        }
    }

    @UiThread
    public void updateSync(Recipe recipe, RecipeAction action, Object... values) {
        if (values == null || values.length == 0) return;
        RecipeTable recipeTable = (RecipeTable) recipe;
        recipeTable.setChangedAt(new Date());
        switch (action) {
            case EDIT_PHOTO:
                PhotoTable photoTable = (PhotoTable) recipe.getMainPhoto();
                if (photoTable == null) return;
                photoTable.setChangedAt(new Date());
                photoTable.setUrl((String) values[0]);
                break;
            case EDIT_AUTHOR:
                recipeTable.setAuthorUuid((String) values[0]);
                break;
            case EDIT_CATEGORY:
                recipeTable.setCategoryUuid((String) values[0]);
                break;
            case EDIT_TIME:
                recipeTable.setMassFlowRateGps((double) values[0]);
                break;
            case EDIT_NAME:
                recipeTable.setName((String) values[0]);
                break;
            case EDIT_FAVORITE:
                recipeTable.setFavorite((Boolean) values[0]);
                break;
            case EDIT_DESCRIPTION:
                recipeTable.setDescription((String) values[0]);
                recipeTable.setSource((String) values[1]);
                break;
        }
    }

    public void updateAsync(Recipe recipe, RecipeAction action) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync()");
        RecipeTable recipeTable = (RecipeTable) recipe;

        switch (action) {
            case EDIT_PHOTO:
                PhotoTable photoTable = (PhotoTable) recipe.getMainPhoto();
                if (photoTable != null) photoTable.update();
                break;
            case EDIT_CATEGORY:
                recipeTable.resetCategoryTables();
                break;
            case EDIT_AUTHOR:
                recipeTable.resetAuthorTables();
                break;
            case EDIT:
            case EDIT_NAME:
            case EDIT_TIME:
            case EDIT_FAVORITE:
            case EDIT_DESCRIPTION:
                break;
        }

        recipeTable.update();
        prefetchRelation(recipeTable);
    }

    @UiThread
    public void updateSync(Method method, String body) {
        RecipeMethodTable recipeMethodTable = (RecipeMethodTable) method;
        recipeMethodTable.setChangedAt(new Date());
        recipeMethodTable.setBody(body);
    }

    @UiThread
    public void updateSync(Method method, int index) {
        RecipeMethodTable recipeMethodTable = (RecipeMethodTable) method;
        recipeMethodTable.setChangedAt(new Date());
        recipeMethodTable.setIndex(index);
    }

    public void updateAsync(Method method) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync()");
        RecipeMethodTable recipeMethodTable = (RecipeMethodTable) method;
        recipeMethodTable.update();
    }

    @UiThread
    public void updateSync(Category category, String value, boolean isPhoto) {
        if (value == null) return;
        CategoryTable categoryTable = (CategoryTable) category;
        categoryTable.setChangedAt(new Date());
        if (isPhoto) {
            PhotoTable photoTable = (PhotoTable) category.getPhoto();
            photoTable.setUrl(value);
        } else {
            categoryTable.setName(value);
        }
    }

    public void updateAsync(Category category) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync()");
        CategoryTable categoryTable = (CategoryTable) category;
        for (PhotoTable photoTable : categoryTable.getPhotoTables()) photoTable.update();
        categoryTable.update();
    }

    @UiThread
    public void updateSync(Author author, String value, boolean isPhoto) {
        if (value == null) return;
        AuthorTable authorTable = (AuthorTable) author;
        authorTable.setChangedAt(new Date());
        if (isPhoto) {
            PhotoTable photoTable = (PhotoTable) author.getPhoto();
            photoTable.setUrl(value);
        } else {
            authorTable.setName(value);
        }
    }

    public void updateAsync(Author author) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync()");
        AuthorTable authorTable = (AuthorTable) author;
        for (PhotoTable photoTable : authorTable.getPhotoTables()) photoTable.update();
        authorTable.update();
    }

    public void updateSync(Food food, String name) {
        FoodTable foodTable = (FoodTable) food;
        foodTable.setName(name);
    }

    public boolean updateAsync(Food food, String ndbNo) {
        FoodTable foodTable = (FoodTable) food;

        boolean cloudUpdate = (ndbNo != null &&
                (food.getNdbNo() == null || !food.getNdbNo().equals(ndbNo)));
        if (cloudUpdate) {
            Map<String, FoodTable> update = cloudManager.getUsdaFoodTable(ndbNo);
            if (update.size() > 0 && update.get(ndbNo) != null) {
                FoodTable ndbNoTable = update.get(ndbNo);

                foodTable.setProtein(ndbNoTable.getProtein());
                foodTable.setFat(ndbNoTable.getFat());
                foodTable.setCarbs(ndbNoTable.getCarbs());
                foodTable.setWeightUnit(ndbNoTable.getWeightUnit());
                foodTable.setEnergy(ndbNoTable.getEnergy());
                foodTable.setEnergyUnit(ndbNoTable.getEnergyUnit());

                foodTable.setName(ndbNoTable.getName());

                FoodUsdaTable foodUsdaTable;
                if (foodTable.getFoodUsdaTables().size() == 0) {
                    foodUsdaTable = new FoodUsdaTable();
                    foodUsdaTable.setUuid(UUID.randomUUID().toString());
                    foodUsdaTable.setChangedAt(new Date());
                    foodUsdaTable.setFoodUuid(foodTable.getUuid());
                    foodTable.getFoodUsdaTables().add(foodUsdaTable);
                } else {
                    foodUsdaTable = foodTable.getFoodUsdaTables().get(0);
                }
                foodUsdaTable.setNdbNo(ndbNo);
                foodUsdaTable.setFetched(true);
                dbManager.modify(foodUsdaTable);
            }
        }

        foodTable.update();
        return cloudUpdate;
    }

    @UiThread
    public void updateSync(Label label, String name) {
        LabelTable labelTable = (LabelTable) label;
        labelTable.setChangedAt(new Date());
        labelTable.setName(name);
    }

    public void updateAsync(Label label) {
        LabelTable labelTable = (LabelTable) label;
        labelTable.setChangedAt(new Date());
        labelTable.update();
    }

    public void deleteAsync(BaseEntity entity) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "deleteAsync()");

        if (entity.getId() == null) return;

        if (entity instanceof AuthorTable) {
            if (getRelatedRecipesSize(entity) > 0) return;
            dbManager.deleteDeep((AuthorTable) entity);

        } else if (entity instanceof CategoryTable) {

            if (getRelatedRecipesSize(entity) > 0) return;
            dbManager.deleteDeep((CategoryTable) entity);

        } else if (entity instanceof LabelTable) {

            if (getRelatedRecipesSize(entity) > 0) return;
            dbManager.deleteDeep((LabelTable) entity);

        } else if (entity instanceof FoodTable) {

            if (getRelatedRecipesSize(entity) > 0) return;
            dbManager.deleteDeep((FoodTable) entity);

        } else if (entity instanceof RecipeMethodTable) {
            RecipeMethodTable recipeMethodTable = (RecipeMethodTable) entity;
            RecipeTable recipeTable = dbManager.getRecipeTable(recipeMethodTable.getRecipeUuid());
            if (recipeTable != null) recipeTable.getRecipeMethodTables().remove(recipeMethodTable);
            recipeMethodTable.delete();

        } else if (entity instanceof RecipeLabelTable) {
            RecipeLabelTable recipeLabelTable = (RecipeLabelTable) entity;
            RecipeTable recipeTable = dbManager.getRecipeTable(recipeLabelTable.getRecipeUuid());
            if (recipeTable != null) recipeTable.getRecipeLabelTables().remove(recipeLabelTable);
            recipeLabelTable.delete();

        } else if (entity instanceof RecipeFoodTable) {

            RecipeFoodTable recipeFoodTable = (RecipeFoodTable) entity;
            RecipeTable recipeTable = dbManager.getRecipeTable(recipeFoodTable.getRecipeUuid());
            if (recipeTable != null) recipeTable.getRecipeFoodTables().remove(recipeFoodTable);
            recipeFoodTable.delete();

        } else if (entity instanceof RecipeTable) {
            dbManager.deleteDeep((RecipeTable) entity);
        }

        if (entity instanceof Table) {
            ((Table) entity).setId(null);
        }
    }

    public BaseEntity createAsync(Class<?> entityClass) {

        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "createAsync()");

        if (entityClass.equals(Author.class)) {
            AuthorTable authorTable = new AuthorTable();
            authorTable.setUuid(UUID.randomUUID().toString());
            authorTable.setChangedAt(new Date());
            authorTable.setName("");
            authorTable.setMail("");
            authorTable.setPhotoUuid(createAsync(Photo.class).getUuid());

            dbManager.modify(authorTable);
            return prefetchRelation(authorTable);

        } else if (entityClass.equals(Category.class)) {
            CategoryTable categoryTable = new CategoryTable();
            categoryTable.setUuid(UUID.randomUUID().toString());
            categoryTable.setChangedAt(new Date());
            categoryTable.setName("");
            categoryTable.setPhotoUuid(createAsync(Photo.class).getUuid());

            dbManager.modify(categoryTable);
            return prefetchRelation(categoryTable);

        } else if (entityClass.equals(Food.class)) {

            FoodTable foodTable = new FoodTable();
            foodTable.setUuid(UUID.randomUUID().toString());
            foodTable.setChangedAt(new Date());
            foodTable.setName("");
            foodTable.setWeightUnit(WeightUnit.GRAM.toString());
            foodTable.setEnergyUnit(EnergyUnit.CALORIE.toString());

            dbManager.modify(foodTable);
            return prefetchRelation(foodTable);

        } else if (entityClass.equals(Ingredient.class)) {
            return null; // goto...
        } else if (entityClass.equals(Label.class)) {

            LabelTable labelTable = new LabelTable();
            labelTable.setUuid(UUID.randomUUID().toString());
            labelTable.setChangedAt(new Date());
            labelTable.setName("");

            dbManager.modify(labelTable);
            return labelTable;

        } else if (entityClass.equals(Measure.class)) {
            return null; // goto...
        } else if (entityClass.equals(Method.class)) {
            return null; // goto...
        } else if (entityClass.equals(Photo.class)) {

            PhotoTable photoTable = new PhotoTable();
            photoTable.setUuid(UUID.randomUUID().toString());
            photoTable.setChangedAt(new Date());
            dbManager.modify(photoTable);
            return photoTable;

        } else if (entityClass.equals(Recipe.class) || entityClass.equals(RecipeFull.class)) {

            RecipeTable recipeTable = new RecipeTable();
            recipeTable.setUuid(UUID.randomUUID().toString());
            recipeTable.setChangedAt(new Date());
            recipeTable.setCreatedAt(new Date());
            recipeTable.setName("");
            recipeTable.setDescription("");
            dbManager.modify(recipeTable);

            RecipePhotoTable recipePhotoTable = new RecipePhotoTable();
            recipePhotoTable.setUuid(UUID.randomUUID().toString());
            recipePhotoTable.setChangedAt(new Date());
            recipePhotoTable.setMain(true);
            recipePhotoTable.setPhotoUuid(createAsync(Photo.class).getUuid());
            recipePhotoTable.setRecipeUuid(recipeTable.getUuid());
            dbManager.modify(recipePhotoTable);

            return prefetchRelation(recipeTable);
        } else {
            return null;
        }
    }

    public Recipe createRecipeAsync(@NonNull String name,
                                    @NonNull Author author,
                                    @NonNull Category category) {

        RecipeTable recipeTable = new RecipeTable();
        recipeTable.setUuid(UUID.randomUUID().toString());
        recipeTable.setChangedAt(new Date());
        recipeTable.setCreatedAt(new Date());
        recipeTable.setName(name);
        recipeTable.setDescription("");
        recipeTable.setAuthorUuid(author.getUuid());
        recipeTable.setCategoryUuid(category.getUuid());
        dbManager.modify(recipeTable);

        RecipePhotoTable recipePhotoTable = new RecipePhotoTable();
        recipePhotoTable.setUuid(UUID.randomUUID().toString());
        recipePhotoTable.setChangedAt(new Date());
        recipePhotoTable.setMain(true);
        recipePhotoTable.setPhotoUuid(createAsync(Photo.class).getUuid());
        recipePhotoTable.setRecipeUuid(recipeTable.getUuid());
        dbManager.modify(recipePhotoTable);

        return prefetchRelationFull(recipeTable);
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
        return dbManager
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
    }

    public boolean updateFoodUsda() {
        return updateFoodUsda(new Date());
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

    private CategoryTable prefetchRelation(CategoryTable categoryTable) {
        if (categoryTable == null) return null;
        categoryTable.getPhotoTables();
        return categoryTable;
    }

    private List<CategoryTable> prefetchCategoryRelations(List<CategoryTable> categoryTables) {
        for (CategoryTable categoryTable : categoryTables) prefetchRelation(categoryTable);
        return categoryTables;
    }

    private FoodTable prefetchRelation(FoodTable foodTable) {
        if (foodTable == null) return null;
        foodTable.getFoodMeasureTables();
        foodTable.getFoodUsdaTables();
        return foodTable;
    }

    private List<FoodTable> prefetchFoodRelations(List<FoodTable> foodTables) {
        for (FoodTable foodTable : foodTables) prefetchRelation(foodTable);
        return foodTables;
    }

    private AuthorTable prefetchRelation(AuthorTable authorTable) {
        if (authorTable == null) return null;
        authorTable.getPhotoTables();
        return authorTable;
    }

    private List<AuthorTable> prefetchAuthorRelations(List<AuthorTable> authorTables) {
        for (AuthorTable authorTable : authorTables) prefetchRelation(authorTable);
        return authorTables;
    }

    /**
     * Prefetch to-many relations
     *
     * @param recipeTable - recipe
     * @return recipeTable
     */
    private RecipeTable prefetchRelation(RecipeTable recipeTable) {
        if (recipeTable == null) return null;
        prefetchAuthorRelations(recipeTable.getAuthorTables());
        prefetchCategoryRelations(recipeTable.getCategoryTables());
        for (RecipePhotoTable recipePhotoTable : recipeTable.getRecipePhotoTables())
            recipePhotoTable.getPhotoTables();
        return recipeTable;
    }

    private List<RecipeTable> prefetchRecipeRelations(List<RecipeTable> recipeTables) {
        for (RecipeTable recipeTable : recipeTables) prefetchRelation(recipeTable);
        return recipeTables;
    }

    private RecipeTable prefetchRelationFull(RecipeTable recipeTable) {
        if (recipeTable == null) return null;
        prefetchRelation(recipeTable);
        recipeTable.getRecipeLabelTables();
        for (RecipeFoodTable recipeFoodTable : recipeTable.getRecipeFoodTables()) {
            prefetchFoodRelations(recipeFoodTable.getFoodTables());
        }
        Collections.sort(recipeTable.getRecipeMethodTables(), methodComparator);
        return recipeTable;
    }
}
