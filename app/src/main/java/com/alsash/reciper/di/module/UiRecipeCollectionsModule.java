package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeCollectionsScope;
import com.alsash.reciper.mvp.presenter.RecipeCollectionsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent collections of recipes.
 */
@Module
public abstract class UiRecipeCollectionsModule {

    @Provides
    @RecipeCollectionsScope
    static RecipeCollectionsPresenter provideRecipeCollectionsPresenter() {
        return new RecipeCollectionsPresenter();
    }

}
