package com.alsash.reciper.api;

import android.support.annotation.WorkerThread;

import com.alsash.reciper.api.storage.local.database.DatabaseApi;
import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.api.storage.local.database.table.LabelTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeTable;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.mvp.model.entity.CacheEntityFactory;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;


/**
 * Single Api for all storage
 */
@Singleton
public class StorageApi {

    private final DatabaseApi databaseApi;
    private final CacheEntityFactory entityFactory;

    public StorageApi(DatabaseApi databaseApi, CacheEntityFactory entityFactory) {
        this.databaseApi = databaseApi;
        this.entityFactory = entityFactory;
    }

    @WorkerThread
    public synchronized List<Category> getCategories(int offset, int limit, int relationsLimit) {

        List<Category> categories = new ArrayList<>();

        for (CategoryTable categoryDb : databaseApi.getCategories(offset, limit, relationsLimit)) {

            Category category = entityFactory.getCategory(
                    categoryDb.getId(),
                    categoryDb.getUuid(),
                    categoryDb.getName(),
                    categoryDb.getCreationDate(),
                    categoryDb.getChangeDate(),
                    new ArrayList<Recipe>());
            categories.add(category);

            for (RecipeTable recipeDb : categoryDb.getRecipes()) {

                List<Label> labels = new ArrayList<>();
                for (LabelTable labelDb : recipeDb.getLabels()) {
                    labels.add(entityFactory.getLabel(
                            labelDb.getId(),
                            labelDb.getUuid(),
                            labelDb.getName(),
                            labelDb.getCreationDate(),
                            labelDb.getChangeDate(),
                            null) // No related recipes for labels with recipes in categories
                    );
                }
                recipeDb.resetLabels();
                category.getRecipes().add(entityFactory.getRecipe(
                        recipeDb.getId(),
                        recipeDb.getUuid(),
                        recipeDb.getName(),
                        recipeDb.getCreationDate(),
                        recipeDb.getChangeDate(),
                        category,
                        labels));
            }
            categoryDb.resetRecipes();
        }

        checkCache();

        return categories;
    }

    @WorkerThread
    public synchronized void createStartupEntriesIfNeed() {
        databaseApi.createStartupEntriesIfNeed();
    }

    private void checkCache() {
        if (entityFactory.getRelativeCacheSize() > AppContract.CACHE_MAX_ENTITIES * 0.90D) {
            entityFactory.clearCache();
            databaseApi.clearCache();
        }
    }
}
