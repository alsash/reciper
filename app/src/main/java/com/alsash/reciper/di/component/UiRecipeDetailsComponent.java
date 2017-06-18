package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeDetailsModule;
import com.alsash.reciper.di.scope.RecipeDetailsScope;
import com.alsash.reciper.ui.activity.RecipeDetailsActivity;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeDetailsActivity and its dependencies
 */
@RecipeDetailsScope
@Subcomponent(
        modules = {
                UiRecipeDetailsModule.class
        }
)
public interface UiRecipeDetailsComponent {

    void inject(RecipeDetailsActivity recipeDetailsActivity);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeDetailsComponent build();
    }
}
