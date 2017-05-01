package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.api.StorageApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provides Data Api
 */
@Module
public abstract class AppStorageModule {

    @Provides
    @Singleton
    static StorageApi provideStorageApi(Context context) {
        return new StorageApi(context);
    }
}
