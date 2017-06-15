package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.RecipeCollectionsScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.RecipeCollectionCategoryPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionGridPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionLabelPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent collections of recipes.
 */
@Module
public abstract class UiRecipeCollectionsModule {

    @Provides
    @RecipeCollectionsScope
    static RecipeCollectionsPresenter provideRecipeCollectionsPresenter() {
        return new RecipeCollectionsPresenter();
    }

    @Provides
    @RecipeCollectionsScope
    static RecipeCollectionCategoryPresenter provideCategoryPresenter(StorageLogic storageLogic,
                                                                      BusinessLogic businessLogic) {
        return new RecipeCollectionCategoryPresenter(storageLogic, businessLogic);
    }

    @Provides
    @RecipeCollectionsScope
    static RecipeCollectionGridPresenter provideRecipeGridPresenter(StorageLogic storageLogic,
                                                                    BusinessLogic businessLogic) {
        return new RecipeCollectionGridPresenter(storageLogic, businessLogic);
    }

    @Provides
    @RecipeCollectionsScope
    static RecipeCollectionLabelPresenter provideLabelPresenter(StorageLogic storageLogic,
                                                                BusinessLogic businessLogic) {
        return new RecipeCollectionLabelPresenter(storageLogic, businessLogic);
    }
}
