package com.alsash.reciper.di.module;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.StartPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide StartPresenter
 */
@Module
public abstract class UiStartModule {

    @Provides
    @StartScope
    static StartPresenter provideStartPresenter(StorageLogic storageLogic,
                                                AppNavigator appNavigator) {
        return new StartPresenter(storageLogic, appNavigator);
    }
}
