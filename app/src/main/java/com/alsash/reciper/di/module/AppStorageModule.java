package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.api.storage.local.database.DatabaseApi;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.mvp.model.entity.CacheEntityFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the Storage Api
 */
@Module
public abstract class AppStorageModule {

    @Provides
    @Singleton
    static StorageApi provideStorageApi(DatabaseApi databaseApi, CacheEntityFactory entityFactory) {
        return new StorageApi(databaseApi, entityFactory);
    }

    @Provides
    @Singleton
    static DatabaseApi provideDatabaseApi(Context context) {
        return new DatabaseApi(context);
    }

    @Provides
    @Singleton
    static CacheEntityFactory provideCacheEntityFactory() {
        return new CacheEntityFactory(AppContract.CACHE_MAX_ENTITIES);
    }
}
