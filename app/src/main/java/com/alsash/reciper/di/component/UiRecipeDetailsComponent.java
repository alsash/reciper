package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeDetailsModule;
import com.alsash.reciper.di.scope.RecipeDetailsScope;
import com.alsash.reciper.ui.activity.RecipeDetailsActivity;
import com.alsash.reciper.ui.fragment.RecipeDetailsDescriptionFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsIngredientsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsMethodsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsNutritionFragment;

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

    void inject(RecipeDetailsDescriptionFragment recipeDetailsDescriptionFragment);

    void inject(RecipeDetailsNutritionFragment recipeDetailsNutritionFragment);

    void inject(RecipeDetailsIngredientsFragment recipeDetailsIngredientsFragment);

    void inject(RecipeDetailsMethodsFragment recipeDetailsMethodsFragment);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeDetailsComponent build();
    }
}
