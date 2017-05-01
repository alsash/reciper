package com.alsash.reciper.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the Application Context
 */
@Module
public class AppContextModule {

    private final Context appContext;

    public AppContextModule(@NonNull Application application) {
        Context appContext = application.getApplicationContext();
        assert appContext != null;
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }
}
