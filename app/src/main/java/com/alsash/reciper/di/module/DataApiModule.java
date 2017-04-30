package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.database.ApiDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provides Database Api
 */
@Module
public class DataApiModule {

    @Provides
    @Singleton
    public static ApiDatabase provideApiDatabase(Context context) {
        return new ApiDatabase(context);
    }
}
