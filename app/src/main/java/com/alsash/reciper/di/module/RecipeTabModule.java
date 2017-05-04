package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.di.scope.RecipeTabScope;
import com.alsash.reciper.mvp.presenter.RecipeTabCategoryPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represents list of recipes
 */
@Module
public abstract class RecipeTabModule {

    @Provides
    @RecipeTabScope
    static RecipeTabPresenter provideRecipeTabPresenter() {
        return new RecipeTabPresenter();
    }

    @Provides
    @RecipeTabScope
    static RecipeTabCategoryPresenter provideRecipeCategoryPresenter(Context context,
                                                                     StorageApi storageApi) {
        return new RecipeTabCategoryPresenter(context, storageApi.getDatabaseApi().getSession());
    }

}
