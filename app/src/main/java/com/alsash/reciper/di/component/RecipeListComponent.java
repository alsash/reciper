package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.RecipeListModule;
import com.alsash.reciper.di.scope.RecipeListScope;
import com.alsash.reciper.ui.activity.RecipeTabActivity;
import com.alsash.reciper.ui.fragment.RecipeCategoryFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between recipe list views and theirs dependencies
 */
@RecipeListScope
@Subcomponent(modules = {RecipeListModule.class})
public interface RecipeListComponent {

    void inject(RecipeTabActivity recipeTabActivity);

    void inject(RecipeCategoryFragment recipeCategoryFragment);

    @Subcomponent.Builder
    interface Builder {
        RecipeListComponent build();
    }

}
