package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeCollectionsModule;
import com.alsash.reciper.di.scope.RecipeCollectionsScope;
import com.alsash.reciper.ui.activity.RecipeCollectionsActivity;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between recipe collection views and theirs dependencies
 */
@RecipeCollectionsScope
@Subcomponent(modules = {UiRecipeCollectionsModule.class})
public interface UiRecipeCollectionsComponent {

    void inject(RecipeCollectionsActivity recipeCollectionsActivity);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeCollectionsComponent build();
    }
}
