package com.alsash.reciper.di.module;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.mvp.presenter.StartPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide StartPresenter
 */
@Module
public abstract class StartModule {

    @Provides
    @StartScope
    static StartPresenter provideStartPresenter(StorageApi storageApi, AppNavigator appNavigator) {
        return new StartPresenter(storageApi, appNavigator);
    }
}
