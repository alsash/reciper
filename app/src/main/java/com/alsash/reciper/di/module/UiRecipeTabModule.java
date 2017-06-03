package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeTabScope;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeTabBookmarkPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabCategoryPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabGridPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabLabelPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represents list of recipes
 */
@Module
public abstract class UiRecipeTabModule {

    @Provides
    @RecipeTabScope
    static RecipeTabPresenter provideRecipeTabPresenter() {
        return new RecipeTabPresenter();
    }

    @Provides
    @RecipeTabScope
    static RecipeTabCategoryPresenter provideRecipeCategoryPresenter(StorageLogic storageLogic) {
        return new RecipeTabCategoryPresenter(storageLogic);
    }

    @Provides
    @RecipeTabScope
    static RecipeTabGridPresenter provideRecipeGridPresenter(StorageLogic storageLogic) {
        return new RecipeTabGridPresenter(storageLogic);
    }

    @Provides
    @RecipeTabScope
    static RecipeTabLabelPresenter provideRecipeLabelPresenter(StorageLogic storageLogic) {
        return new RecipeTabLabelPresenter(storageLogic);
    }

    @Provides
    @RecipeTabScope
    static RecipeTabBookmarkPresenter provideRecipeBookmarkPresenter(StorageLogic storageLogic) {
        return new RecipeTabBookmarkPresenter(storageLogic);
    }
}
