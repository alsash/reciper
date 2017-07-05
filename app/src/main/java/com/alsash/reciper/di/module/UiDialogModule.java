package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.DialogScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeCreationDialogPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent restriction of recipes.
 */
@Module
public abstract class UiDialogModule {

    @Provides
    @DialogScope
    static RecipeCreationDialogPresenter provideCreationPresenter(StorageLogic storageLogic,
                                                                  BusinessLogic businessLogic) {
        return new RecipeCreationDialogPresenter(storageLogic, businessLogic);
    }

}
