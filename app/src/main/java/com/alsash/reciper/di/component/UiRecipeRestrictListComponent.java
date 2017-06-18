package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeRestrictListModule;
import com.alsash.reciper.di.scope.RecipeRestrictListScope;
import com.alsash.reciper.ui.fragment.RecipeRestrictListFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeRestrictListFragment and its dependencies
 */
@RecipeRestrictListScope
@Subcomponent(
        modules = {
                UiRecipeRestrictListModule.class
        }
)
public interface UiRecipeRestrictListComponent {

    void inject(RecipeRestrictListFragment recipeRestrictListFragment);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeRestrictListComponent build();
    }
}
