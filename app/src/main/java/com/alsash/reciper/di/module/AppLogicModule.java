package com.alsash.reciper.di.module;

import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the StorageLogic with their dependencies
 */
@Module
public abstract class AppLogicModule {

    @Provides
    @Singleton
    static BusinessLogic provideBusinessLogic() {
        return new BusinessLogic();
    }

    @Provides
    @Singleton
    static StorageLogic provideStorageLogic(DbManager dbManager, CloudManager cloudManager) {
        return new StorageLogic(dbManager, cloudManager);
    }
}
