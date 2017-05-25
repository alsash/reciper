package com.alsash.reciper.di.module;

import com.alsash.reciper.data.StorageApi;
import com.alsash.reciper.di.scope.RecipeTabScope;
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
public abstract class RecipeTabModule {

    @Provides
    @RecipeTabScope
    static RecipeTabPresenter provideRecipeTabPresenter() {
        return new RecipeTabPresenter();
    }

    @Provides
    @RecipeTabScope
    static RecipeTabCategoryPresenter provideRecipeCategoryPresenter(StorageApi storageApi) {
        return new RecipeTabCategoryPresenter(storageApi);
    }

    @Provides
    @RecipeTabScope
    static RecipeTabGridPresenter provideRecipeGridPresenter(StorageApi storageApi) {
        return new RecipeTabGridPresenter(storageApi);
    }

    @Provides
    @RecipeTabScope
    static RecipeTabLabelPresenter provideRecipeLabelPresenter(StorageApi storageApi) {
        return new RecipeTabLabelPresenter(storageApi);
    }

    @Provides
    @RecipeTabScope
    static RecipeTabBookmarkPresenter provideRecipeBookmarkPresenter(StorageApi storageApi) {
        return new RecipeTabBookmarkPresenter(storageApi);
    }
}
