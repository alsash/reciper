package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.api.storage.local.database.DatabaseApi;
import com.alsash.reciper.di.scope.RecipeListScope;
import com.alsash.reciper.mvp.presenter.RecipeCategoryPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represents list of recipes
 */
@Module
public abstract class RecipeListModule {

    @Provides
    @RecipeListScope
    static RecipeTabPresenter provideRecipeTabPresenter() {
        return new RecipeTabPresenter();
    }

    @Provides
    @RecipeListScope
    static RecipeCategoryPresenter provideRecipeCategoryPresenter(Context context,
                                                                  DatabaseApi databaseApi) {
        return new RecipeCategoryPresenter(context, databaseApi.getSession());
    }

}
