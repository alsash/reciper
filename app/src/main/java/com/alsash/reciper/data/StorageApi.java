package com.alsash.reciper.data;

import android.support.annotation.WorkerThread;

import com.alsash.reciper.api.storage.local.database.DatabaseApi;
import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.api.storage.local.database.table.LabelTable;
import com.alsash.reciper.api.storage.local.database.table.PhotoTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeTable;
import com.alsash.reciper.mvp.model.entity.CacheEntityFactory;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;


/**
 * Single Api for all storage
 */
@Singleton
public class StorageApi {

    private final DatabaseApi databaseApi;
    private final CacheEntityFactory entityFactory;

    private long checkCacheCount;

    public StorageApi(DatabaseApi databaseApi, CacheEntityFactory entityFactory) {
        this.databaseApi = databaseApi;
        this.entityFactory = entityFactory;

    }

    @WorkerThread
    public List<Category> getCategories(int offset, int limit) {
        List<Category> categories = new ArrayList<>();
        for (CategoryTable categoryTable : databaseApi.getCategories(offset, limit)) {
            categories.add(getCategory(categoryTable));
        }
        checkCache();
        return categories;
    }

    @WorkerThread
    public List<Label> getLabels(int offset, int limit) {
        List<Label> labels = new ArrayList<>();
        for (LabelTable labelTable : databaseApi.getLabels(offset, limit)) {
            labels.add(getLabel(labelTable));
        }
        return labels;
    }

    @WorkerThread
    public List<Recipe> getRecipes(int offset, int limit) {
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeTable recipeTable : databaseApi.getRecipes(offset, limit)) {
            recipes.add(getRecipe(recipeTable));
        }
        checkCache();
        return recipes;
    }

    @WorkerThread
    public List<Recipe> getLabeledRecipes(int offset, int limit, long labelId) {
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeTable recipeTable : databaseApi.getLabeledRecipes(offset, limit, labelId)) {
            recipes.add(getRecipe(recipeTable));
        }
        return recipes;
    }

    @WorkerThread
    public List<Recipe> getCategorizedRecipes(int offset, int limit, long categoryId) {
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeTable recipeTable :
                databaseApi.getCategorizedRecipes(offset, limit, categoryId)) {
            recipes.add(getRecipe(recipeTable));
        }
        return recipes;
    }

    @WorkerThread
    public List<Recipe> getBookmarkedRecipes(int offset, int limit) {
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeTable recipeTable : databaseApi.getBookmarkedRecipes(offset, limit)) {
            recipes.add(getRecipe(recipeTable));
        }
        return recipes;
    }

    private Recipe getRecipe(RecipeTable recipeTable) {
        return (recipeTable == null) ? null :
                entityFactory.getRecipe(
                        recipeTable.getId(),
                        UUID.fromString(recipeTable.getUuid()),
                        recipeTable.getName(),
                        recipeTable.getCreationDate(),
                        recipeTable.getChangeDate(),
                        recipeTable.getBookmark(),
                        getCategory(recipeTable.getCategory()),
                        getLabels(recipeTable.getLabels())
                );
    }

    private List<Label> getLabels(List<LabelTable> labelTables) {
        if (labelTables == null) return null;
        List<Label> labels = new ArrayList<>();
        for (LabelTable labelDb : labelTables) {
            labels.add(getLabel(labelDb));
        }
        return labels;
    }

    private Label getLabel(LabelTable labelTable) {
        return (labelTable == null) ? null :
                entityFactory.getLabel(
                        labelTable.getId(),
                        labelTable.getUuid(),
                        labelTable.getName(),
                        labelTable.getCreationDate(),
                        labelTable.getChangeDate());
    }

    private Category getCategory(CategoryTable categoryTable) {
        return (categoryTable == null) ? null :
                entityFactory.getCategory(
                        categoryTable.getId(),
                        categoryTable.getUuid(),
                        categoryTable.getName(),
                        categoryTable.getCreationDate(),
                        categoryTable.getChangeDate(),
                        getPhoto(categoryTable.getPhoto()));
    }

    private Photo getPhoto(PhotoTable photoTable) {
        return (photoTable == null) ? null :
                entityFactory.getPhoto(
                        photoTable.getId(),
                        photoTable.getUuid(),
                        photoTable.getName(),
                        photoTable.getCreationDate(),
                        photoTable.getChangeDate(),
                        photoTable.getUrl(),
                        photoTable.getPath()
                );
    }

    @WorkerThread
    public synchronized void createStartupEntriesIfNeed() {
        databaseApi.createStartupEntriesIfNeed();
    }

    private void checkCache() {
        checkCacheCount += 1;
        if ((checkCacheCount % 5) != 0) return;
        if (entityFactory.getSize() > entityFactory.getMaxCacheSizeByte() * 0.90D) {
            entityFactory.clearCache();
            databaseApi.clearCache();
        }
    }
}
