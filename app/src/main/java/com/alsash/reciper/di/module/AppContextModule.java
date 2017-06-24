package com.alsash.reciper.di.module;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.alsash.reciper.logic.NavigationLogic;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the Application Context and related objects
 */
@Module
public class AppContextModule {

    private final Context appContext;

    public AppContextModule(@NonNull Application application) {
        Context appContext = application.getApplicationContext();
        assert appContext != null;
        this.appContext = appContext;
    }

    /**
     * Provide application context only for SQLiteOpenHelper inheritor.
     *
     * @return Application context
     */
    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    NavigationLogic provideAppNavigator() {
        return new NavigationLogic(appContext);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
