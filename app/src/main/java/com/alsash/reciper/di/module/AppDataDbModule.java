package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.data.db.DbHelper;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.DaoMaster;
import com.alsash.reciper.data.db.table.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the StorageLogic with their dependencies
 */
@Module
public abstract class AppDataDbModule {

    @Provides
    @Singleton
    static DbManager provideDbManager(DaoSession daoSession) {
        return new DbManager(daoSession);
    }

    /**
     * Provide SQLiteOpenHelper
     *
     * @param context - application context (from AppContextModule)
     * @return DbHelper instance
     */
    @Provides
    @Singleton
    static DbHelper provideDbHelper(Context context) {
        return new DbHelper(context, AppContract.Db.DATABASE_NAME);
    }

    @Provides
    @Singleton
    static DaoMaster provideDbDaoMaster(DbHelper dbHelper) {
        return new DaoMaster(dbHelper.getWritableDb());
    }

    @Provides
    @Singleton
    static DaoSession provideDbDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }
}
