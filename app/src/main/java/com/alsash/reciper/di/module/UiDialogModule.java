package com.alsash.reciper.di.module;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeAuthorDialogPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCategoryDialogPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCreationDialogPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent restriction of recipes.
 * Without scope
 */
@Module
public abstract class UiDialogModule {

    @Provides
    static RecipeCreationDialogPresenter provideCreationDialogPresenter(StorageLogic sLogic,
                                                                        BusinessLogic bLogic) {
        return new RecipeCreationDialogPresenter(sLogic, bLogic);
    }

    @Provides
    static RecipeCategoryDialogPresenter provideCategoryDialogPresenter(StorageLogic sLogic,
                                                                        BusinessLogic bLogic) {
        return new RecipeCategoryDialogPresenter(sLogic, bLogic);
    }

    @Provides
    static RecipeAuthorDialogPresenter provideAuthorDialogPresenter(StorageLogic sLogic,
                                                                    BusinessLogic bLogic) {
        return new RecipeAuthorDialogPresenter(sLogic, bLogic);
    }

}
