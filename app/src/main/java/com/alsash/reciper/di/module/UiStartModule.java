package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.logic.NavigationLogic;
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
                                                NavigationLogic navigationLogic) {
        return new StartPresenter(storageLogic, navigationLogic);
    }
}
