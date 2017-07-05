package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiDialogModule;
import com.alsash.reciper.ui.fragment.dialog.RecipeAuthorDialogFragment;
import com.alsash.reciper.ui.fragment.dialog.RecipeCategoryDialogFragment;
import com.alsash.reciper.ui.fragment.dialog.RecipeCreationDialogFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeRestrictListFragment and its dependencies.
 * No scope
 */
@Subcomponent(
        modules = {
                UiDialogModule.class
        }
)
public interface UiDialogComponent {

    void inject(RecipeCreationDialogFragment recipeCreationDialogFragment);

    void inject(RecipeCategoryDialogFragment recipeCategoryDialogFragment);

    void inject(RecipeAuthorDialogFragment recipeAuthorDialogFragment);

    @Subcomponent.Builder
    interface Builder {
        UiDialogComponent build();
    }
}
