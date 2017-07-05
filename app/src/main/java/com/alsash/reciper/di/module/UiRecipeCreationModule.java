package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeCreationScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeCreationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent restriction of recipes.
 */
@Module
public abstract class UiRecipeCreationModule {

    @Provides
    @RecipeCreationScope
    static RecipeCreationPresenter provideCreationPresenter(StorageLogic storageLogic,
                                                            BusinessLogic businessLogic) {
        return new RecipeCreationPresenter(storageLogic, businessLogic);
    }

}
