package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeSearchScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeSearchListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent search of recipes.
 */
@Module
public class UiRecipeSearchModule {

    @Provides
    @RecipeSearchScope
    static RecipeSearchListPresenter provideSearchListPresenter(StorageLogic storageLogic,
                                                                BusinessLogic businessLogic) {
        return new RecipeSearchListPresenter(storageLogic, businessLogic);
    }

}
