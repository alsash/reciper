package com.alsash.reciper.api;

import android.support.annotation.WorkerThread;

import com.alsash.reciper.api.storage.local.database.DatabaseApi;
import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.api.storage.local.database.table.PhotoTable;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.mvp.model.entity.CacheEntityFactory;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Photo;

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
    public synchronized List<Category> getCategories(int offset, int limit) {
        List<Category> categories = new ArrayList<>();
        for (CategoryTable categoryDb : databaseApi.getCategories(offset, limit)) {
            PhotoTable photoDb = categoryDb.getPhoto();
            Photo photo = (photoDb == null) ? null :
                    entityFactory.getPhoto(
                            photoDb.getId(),
                            photoDb.getUuid(),
                            photoDb.getName(),
                            photoDb.getCreationDate(),
                            photoDb.getChangeDate(),
                            photoDb.getUrl(),
                            photoDb.getPath()
                    );
            Category category = entityFactory.getCategory(
                    categoryDb.getId(),
                    categoryDb.getUuid(),
                    categoryDb.getName(),
                    categoryDb.getCreationDate(),
                    categoryDb.getChangeDate(),
                    photo);
            categories.add(category);
        }
        checkCache();
        return categories;
    }

    @WorkerThread
    public synchronized void createStartupEntriesIfNeed() {
        databaseApi.createStartupEntriesIfNeed();
    }

    private void checkCache() {
        if (entityFactory.getSize() > AppContract.CACHE_SIZE_BYTE * 0.90D) {
            entityFactory.clearCache();
            databaseApi.clearCache();
        }
    }
}
