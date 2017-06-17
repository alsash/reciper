package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeSearchModule;
import com.alsash.reciper.di.scope.RecipeSearchScope;
import com.alsash.reciper.ui.fragment.RecipeRestrictListFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeRestrictListFragment and its dependencies
 */
@RecipeSearchScope
@Subcomponent(
        modules = {
                UiRecipeSearchModule.class
        }
)
public interface UiRecipeSearchComponent {

    void inject(RecipeRestrictListFragment recipeRestrictListFragment);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeSearchComponent build();
    }
}
