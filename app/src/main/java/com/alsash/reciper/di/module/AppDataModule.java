package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.database.ApiDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provides Data Api
 */
@Module
public abstract class AppDataModule {

    @Provides
    @Singleton
    static ApiDatabase provideApiDatabase(Context context) {
        return new ApiDatabase(context);
    }
}
