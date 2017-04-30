package com.alsash.reciper.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the Application Context
 */
@Module
public class AppContextModule {

    private final WeakReference<Context> appContextRef;

    public AppContextModule(@NonNull Application application) {
        Context appContext = application.getApplicationContext();
        assert appContext != null;
        this.appContextRef = new WeakReference<>(appContext);
    }

    @Provides
    @Singleton
    public Context provideAppContext() {
        return appContextRef.get();
    }
}
