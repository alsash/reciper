package com.alsash.reciper.di.module;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.di.scope.RecipeTabScope;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide Presenters for RecipeTabActivity and it's fragments
 */
@Module
public abstract class RecipeTabModule {

    @Provides
    @RecipeTabScope
    static RecipeTabPresenter provideRecipeTabPresenter(StorageApi storageApi) {
        return new RecipeTabPresenter();
    }
}
