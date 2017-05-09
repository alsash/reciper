package com.alsash.reciper.api;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.api.storage.local.database.DatabaseApi;
import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.mvp.model.entity.CacheEntityFactory;
import com.alsash.reciper.mvp.model.entity.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;


/**
 * Single Api for all storage
 */
@Singleton
public class StorageApi {
    private static final int MAX_CACHE_ENTITIES = 1000;

    private final Context context;
    private final DatabaseApi databaseApi;
    private final CacheEntityFactory entityFactory;

    public StorageApi(Context context) {
        this.context = context;
        this.databaseApi = new DatabaseApi(context);
        this.entityFactory = new CacheEntityFactory(MAX_CACHE_ENTITIES);
    }

    @WorkerThread
    public synchronized List<Category> getCategories(int limit, int offset, int maxRelatedEntities) {
        List<CategoryTable> categoryTables = databaseApi
                .getSession()
                .getCategoryTableDao()
                .queryBuilder()
                .limit(limit)
                .offset(offset)
                .build()
                .forCurrentThread()
                .list();
        List<Category> categories = new ArrayList<>();
        for (CategoryTable categoryTable : categoryTables) {

        }
        return categories;
    }

    @WorkerThread
    public synchronized void createStartupEntriesIfNeed() {
        databaseApi.createStartupEntriesIfNeed();
    }
}
