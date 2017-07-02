package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeRestrictListScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeRestrictListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent restriction of recipes.
 */
@Module
public abstract class UiRecipeRestrictListModule {

    @Provides
    @RecipeRestrictListScope
    static RecipeRestrictListPresenter provideRestrictListPresenter(StorageLogic storageLogic,
                                                                    BusinessLogic businessLogic) {
        return new RecipeRestrictListPresenter(storageLogic, businessLogic);
    }

}
