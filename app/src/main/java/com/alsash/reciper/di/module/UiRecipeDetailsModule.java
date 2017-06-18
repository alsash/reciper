package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeDetailsScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeDetailsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent search of recipes.
 */
@Module
public class UiRecipeDetailsModule {

    @Provides
    @RecipeDetailsScope
    static RecipeDetailsPresenter provideRecipeDetailsPresenter(StorageLogic storageLogic,
                                                                BusinessLogic businessLogic) {
        return new RecipeDetailsPresenter(storageLogic, businessLogic);
    }

}
