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
import java.util.Set;
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
        if (entity == null || entity.getId() == null) return false;
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
        if (category == null || category.getId() == null) return recipes;
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
        if (label == null || label.getId() == null) return recipes;
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
        if (food == null || food.getId() == null) return recipes;
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
        if (author == null || author.getId() == null) return recipes;
        List<RecipeTable> recipeTables = dbManager
                .restrictWith(offset, limit)
                .getRecipeTable((AuthorTable) author);
        recipes.addAll(prefetchRecipeRelations(recipeTables));
        return recipes;
    }

    public BaseEntity getRestrictEntity(EntityRestriction restriction) {
        if (restriction == null) return null;
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
        if (recipe == null || recipe.getId() == null) return;
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
            case EDIT_SERVINGS:
                recipeTable.setServings((int) values[0]);
        }
    }

    public void updateAsync(Recipe recipe, RecipeAction action) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync()");
        if (recipe == null || recipe.getId() == null) return;
        RecipeTable recipeTable = (RecipeTable) recipe;
        if (recipeTable.getId() == null) return;

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
            case EDIT_SERVINGS:
                break;
        }

        recipeTable.update();
        prefetchRelation(recipeTable);
    }

    public void updateAsync(Recipe recipe, RecipeAction action, Set<String> relationUuid) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync(rel)");
        if (recipe == null || recipe.getId() == null) return;
        RecipeTable recipeTable = (RecipeTable) recipe;

        switch (action) {
            case EDIT_LABELS:
                List<RecipeLabelTable> recipeLabelTables = recipeTable.getRecipeLabelTables();
                dbManager.deleteAll(recipeLabelTables);
                for (RecipeLabelTable recipeLabelTable : recipeLabelTables) {
                    recipeLabelTable.setId(null);
                }
                recipeLabelTables.clear();

                for (String labelUuid : relationUuid) {
                    LabelTable labelTable = dbManager.getLabelTable(labelUuid);
                    if (labelTable == null) continue;
                    RecipeLabelTable recipeLabelTable = new RecipeLabelTable();
                    recipeLabelTable.setUuid(UUID.randomUUID().toString());
                    recipeLabelTable.setChangedAt(new Date());
                    recipeLabelTable.setRecipeUuid(recipeTable.getUuid());
                    recipeLabelTable.setLabelUuid(labelTable.getUuid());

                    recipeLabelTables.add(recipeLabelTable);
                }
                dbManager.modifyList(recipeLabelTables);

                recipeTable.resetRecipeLabelTables();
                recipeTable.getRecipeLabelTables();
                break;
            case CREATE_INGREDIENT:
                for (String foodUuid : relationUuid) {
                    FoodTable foodTable = dbManager.getFoodTable(foodUuid);
                    if (foodTable == null) continue;
                    RecipeFoodTable recipeFoodTable = new RecipeFoodTable();
                    recipeFoodTable.setUuid(UUID.randomUUID().toString());
                    recipeFoodTable.setChangedAt(new Date());
                    recipeFoodTable.setName(foodTable.getName());
                    recipeFoodTable.setFoodUuid(foodTable.getUuid());
                    recipeFoodTable.setRecipeUuid(recipeTable.getUuid());
                    recipeFoodTable.setWeight(100);
                    recipeFoodTable.setWeightUnit(WeightUnit.GRAM.toString());
                    dbManager.modify(recipeFoodTable);
                    recipeTable.getRecipeFoodTables().add(recipeFoodTable);
                    break; // Add only 1 ingredient
                }
                break;

            case DELETE_INGREDIENT:
                for (String ingredientUuid : relationUuid) {
                    List<RecipeFoodTable> recipeFoodTableDelete = new ArrayList<>();
                    for (RecipeFoodTable recipeFoodTable : recipeTable.getRecipeFoodTables()) {
                        if (recipeFoodTable.getUuid().equals(ingredientUuid)) {
                            recipeFoodTableDelete.add(recipeFoodTable);
                        }
                    }
                    recipeTable.getRecipeFoodTables().removeAll(recipeFoodTableDelete);
                    dbManager.deleteAll(recipeFoodTableDelete);
                    for (RecipeFoodTable deleted : recipeFoodTableDelete) {
                        deleted.setId(null);
                    }
                }
                break;
            case CREATE_METHOD:
                List<RecipeMethodTable> recipeMethodTables = recipeTable.getRecipeMethodTables();

                RecipeMethodTable recipeMethodTable = new RecipeMethodTable();
                recipeMethodTable.setUuid(UUID.randomUUID().toString());
                recipeMethodTable.setChangedAt(new Date());
                recipeMethodTable.setIndex(0);
                recipeMethodTable.setBody("");
                recipeMethodTable.setRecipeUuid(recipeTable.getUuid());

                recipeMethodTables.add(0, recipeMethodTable);
                for (int i = 0; i < recipeMethodTables.size(); i++) {
                    recipeMethodTables.get(i).setIndex(i);
                }
                dbManager.modifyList(recipeMethodTables);
                break;
            case DELETE_METHOD:
                for (String methodUuid : relationUuid) {
                    List<RecipeMethodTable> recipeMethodTableDelete = new ArrayList<>();
                    for (RecipeMethodTable rmt : recipeTable.getRecipeMethodTables()) {
                        if (rmt.getUuid().equals(methodUuid)) {
                            recipeMethodTableDelete.add(rmt);
                        }
                    }
                    recipeTable.getRecipeMethodTables().removeAll(recipeMethodTableDelete);
                    dbManager.deleteAll(recipeMethodTableDelete);
                    for (RecipeMethodTable deleted : recipeMethodTableDelete) {
                        deleted.setId(null);
                    }
                }
                break;
        }
    }

    @UiThread
    public void updateSync(List<Ingredient> ingredients, Map<String, Double> weight) {
        if (ingredients == null || weight == null) return;
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == null) continue;
            RecipeFoodTable recipeFoodTable = (RecipeFoodTable) ingredient;
            for (Map.Entry<String, Double> entry : weight.entrySet()) {
                if (entry.getKey().equals(recipeFoodTable.getUuid())
                        && entry.getValue() != null) {
                    recipeFoodTable.setChangedAt(new Date());
                    recipeFoodTable.setWeight(entry.getValue());
                }
            }
        }
    }

    public void updateAsync(List<Ingredient> ingredients) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync(ing)");
        if (ingredients == null) return;
        List<RecipeFoodTable> recipeFoodTables = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == null) continue;
            RecipeFoodTable recipeFoodTable = (RecipeFoodTable) ingredient;
            recipeFoodTables.add(recipeFoodTable);
        }
        dbManager.modifyList(recipeFoodTables);
    }

    public void updateSync(Ingredient ingredient, String name, int weight) {
        if (ingredient == null || ingredient.getId() == null) return;
        RecipeFoodTable recipeFoodTable = (RecipeFoodTable) ingredient;
        if (name != null) recipeFoodTable.setName(name);
        recipeFoodTable.setWeight(weight);
        recipeFoodTable.setChangedAt(new Date());
    }

    public void updateAsync(Ingredient ingredient) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync(ing)");
        if (ingredient == null || ingredient.getId() == null) return;
        RecipeFoodTable recipeFoodTable = (RecipeFoodTable) ingredient;
        recipeFoodTable.update();
    }

    @UiThread
    public void updateSync(Method method, String body) {
        if (method == null || method.getId() == null) return;
        RecipeMethodTable recipeMethodTable = (RecipeMethodTable) method;
        recipeMethodTable.setChangedAt(new Date());
        recipeMethodTable.setBody(body);
    }

    @UiThread
    public void updateSync(Method method, int index) {
        if (method == null || method.getId() == null) return;
        RecipeMethodTable recipeMethodTable = (RecipeMethodTable) method;
        recipeMethodTable.setChangedAt(new Date());
        recipeMethodTable.setIndex(index);
    }

    public void updateAsync(Method method) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync()");
        if (method == null || method.getId() == null) return;
        RecipeMethodTable recipeMethodTable = (RecipeMethodTable) method;
        recipeMethodTable.update();
    }

    @UiThread
    public void updateSync(Category category, String value, boolean isPhoto) {
        if (category == null || category.getId() == null) return;
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
        if (category == null || category.getId() == null) return;
        CategoryTable categoryTable = (CategoryTable) category;
        for (PhotoTable photoTable : categoryTable.getPhotoTables()) photoTable.update();
        categoryTable.update();
    }

    @UiThread
    public void updateSync(Author author, String value, boolean isPhoto) {
        if (author == null || author.getId() == null) return;
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
        if (author == null || author.getId() == null) return;
        AuthorTable authorTable = (AuthorTable) author;
        for (PhotoTable photoTable : authorTable.getPhotoTables()) photoTable.update();
        authorTable.update();
    }

    public void updateSync(Food food, String name) {
        if (food == null || food.getId() == null) return;
        FoodTable foodTable = (FoodTable) food;
        foodTable.setName(name);
    }

    public boolean updateAsync(Food food, String ndbNo) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync(food)");
        if (food == null || food.getId() == null) return false;
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
        if (label == null || label.getId() == null) return;
        LabelTable labelTable = (LabelTable) label;
        labelTable.setChangedAt(new Date());
        labelTable.setName(name);
    }

    public void updateAsync(Label label) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "updateAsync(label)");
        if (label == null || label.getId() == null) return;
        LabelTable labelTable = (LabelTable) label;
        labelTable.setChangedAt(new Date());
        labelTable.update();
    }

    public void deleteAsync(BaseEntity entity) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "deleteAsync()");

        if (entity == null || entity.getId() == null) return;

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

        } else if (entity instanceof RecipeTable) {
            dbManager.deleteDeep((RecipeTable) entity);
        }

        if (entity instanceof Table) {
            ((Table) entity).setId(null);
        }
    }

    public BaseEntity createAsync(Class<?> entityClass) {

        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "createAsync()");
        if (entityClass == null) return null;

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
            return null; // goto updateAsync(Recipe, RecipeAction, String...)

        } else if (entityClass.equals(Label.class)) {

            LabelTable labelTable = new LabelTable();
            labelTable.setUuid(UUID.randomUUID().toString());
            labelTable.setChangedAt(new Date());
            labelTable.setName("");

            dbManager.modify(labelTable);
            return labelTable;

        } else if (entityClass.equals(Measure.class)) {
            return null; // not implemented

        } else if (entityClass.equals(Method.class)) {
            return null; // goto updateAsync(Recipe, RecipeAction, String...)

        } else if (entityClass.equals(Photo.class)) {

            PhotoTable photoTable = new PhotoTable();
            photoTable.setUuid(UUID.randomUUID().toString());
            photoTable.setChangedAt(new Date());
            dbManager.modify(photoTable);
            return photoTable;

        } else {
            return null;
        }
    }

    public Recipe createRecipeAsync(@NonNull String name,
                                    @NonNull Author author,
                                    @NonNull Category category) {
        if (BuildConfig.DEBUG) MainThreadException.throwOnMainThread(TAG, "createRecipeAsync()");
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

    private boolean updateFoodUsda(Date updateDate) {
        List<FoodTable> dbFoods;
        if (!cloudManager.isOnline()) return false; // not updated
        do {
            try {
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
            } catch (Throwable e) {
                return false;
            }
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
        for (RecipeLabelTable recipeLabelTable : recipeTable.getRecipeLabelTables()) {
            recipeLabelTable.getLabelTables();
        }
        for (RecipeFoodTable recipeFoodTable : recipeTable.getRecipeFoodTables()) {
            prefetchFoodRelations(recipeFoodTable.getFoodTables());
        }
        Collections.sort(recipeTable.getRecipeMethodTables(), methodComparator);
        return recipeTable;
    }
}
