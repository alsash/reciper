package com.alsash.reciper.di.module;

import com.alsash.reciper.database.ApiDatabase;
import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.mvp.presenter.StartPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provides StartPresenter
 */
@Module
public abstract class StartModule {

    @Provides
    @StartScope
    static StartPresenter provideStartPresenter(ApiDatabase apiDatabase) {
        return new StartPresenter(apiDatabase.getSession());
    }
}
