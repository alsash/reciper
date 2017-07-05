package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeCreationModule;
import com.alsash.reciper.di.scope.RecipeCreationScope;
import com.alsash.reciper.ui.fragment.dialog.RecipeCreationDialogFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeRestrictListFragment and its dependencies
 */
@RecipeCreationScope
@Subcomponent(
        modules = {
                UiRecipeCreationModule.class
        }
)
public interface UiRecipeCreationComponent {

    void inject(RecipeCreationDialogFragment recipeCreationDialogFragment);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeCreationComponent build();
    }
}
