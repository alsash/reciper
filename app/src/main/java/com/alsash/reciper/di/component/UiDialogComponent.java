package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiDialogModule;
import com.alsash.reciper.di.scope.DialogScope;
import com.alsash.reciper.ui.fragment.dialog.RecipeCreationDialogFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeRestrictListFragment and its dependencies
 */
@DialogScope
@Subcomponent(
        modules = {
                UiDialogModule.class
        }
)
public interface UiDialogComponent {

    void inject(RecipeCreationDialogFragment recipeCreationDialogFragment);

    @Subcomponent.Builder
    interface Builder {
        UiDialogComponent build();
    }
}
