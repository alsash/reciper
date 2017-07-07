package com.alsash.reciper.di.module;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeDialogAuthorPresenter;
import com.alsash.reciper.mvp.presenter.RecipeDialogCategoryPresenter;
import com.alsash.reciper.mvp.presenter.RecipeDialogCreationPresenter;
import com.alsash.reciper.mvp.presenter.RecipeDialogLabelPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent restriction of recipes.
 * Without scope
 */
@Module
public abstract class UiDialogModule {

    @Provides
    static RecipeDialogCreationPresenter provideCreationDialogPresenter(StorageLogic sLogic,
                                                                        BusinessLogic bLogic) {
        return new RecipeDialogCreationPresenter(sLogic, bLogic);
    }

    @Provides
    static RecipeDialogCategoryPresenter provideCategoryDialogPresenter(StorageLogic sLogic,
                                                                        BusinessLogic bLogic) {
        return new RecipeDialogCategoryPresenter(sLogic, bLogic);
    }

    @Provides
    static RecipeDialogAuthorPresenter provideAuthorDialogPresenter(StorageLogic sLogic,
                                                                    BusinessLogic bLogic) {
        return new RecipeDialogAuthorPresenter(sLogic, bLogic);
    }

    @Provides
    static RecipeDialogLabelPresenter provideLabelDialogPresenter(StorageLogic sLogic,
                                                                  BusinessLogic bLogic) {
        return new RecipeDialogLabelPresenter(sLogic, bLogic);
    }

}
