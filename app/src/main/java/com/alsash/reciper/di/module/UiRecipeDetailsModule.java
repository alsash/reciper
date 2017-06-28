package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeDetailsScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeDetailsDescriptionPresenter;
import com.alsash.reciper.mvp.presenter.RecipeDetailsIngredientsPresenter;
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

    @Provides
    @RecipeDetailsScope
    static RecipeDetailsDescriptionPresenter provideDescriptionsPresenter(StorageLogic sLogic,
                                                                          BusinessLogic bLogic) {
        return new RecipeDetailsDescriptionPresenter(sLogic, bLogic);
    }

    @Provides
    @RecipeDetailsScope
    static RecipeDetailsIngredientsPresenter provideIngredientsPresenter(StorageLogic sLogic,
                                                                         BusinessLogic bLogic) {
        return new RecipeDetailsIngredientsPresenter(sLogic, bLogic);
    }

}
