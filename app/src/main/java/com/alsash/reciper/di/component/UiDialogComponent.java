package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiDialogModule;
import com.alsash.reciper.ui.fragment.dialog.RecipeDialogAuthorFragment;
import com.alsash.reciper.ui.fragment.dialog.RecipeDialogCategoryFragment;
import com.alsash.reciper.ui.fragment.dialog.RecipeDialogCreationFragment;
import com.alsash.reciper.ui.fragment.dialog.RecipeDialogIngredientFragment;
import com.alsash.reciper.ui.fragment.dialog.RecipeDialogLabelFragment;

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

    void inject(RecipeDialogCreationFragment recipeCreationDialogFragment);

    void inject(RecipeDialogCategoryFragment recipeCategoryDialogFragment);

    void inject(RecipeDialogAuthorFragment recipeAuthorDialogFragment);

    void inject(RecipeDialogLabelFragment recipeDialogLabelFragment);

    void inject(RecipeDialogIngredientFragment recipeDialogIngredientFragment);

    @Subcomponent.Builder
    interface Builder {
        UiDialogComponent build();
    }
}
