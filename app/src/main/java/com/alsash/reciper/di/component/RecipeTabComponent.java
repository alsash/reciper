package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.RecipeTabModule;
import com.alsash.reciper.di.scope.RecipeTabScope;
import com.alsash.reciper.ui.activity.RecipeTabActivity;
import com.alsash.reciper.ui.fragment.RecipeTabCategoryFragmentList;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between recipe list views and theirs dependencies
 */
@RecipeTabScope
@Subcomponent(modules = {RecipeTabModule.class})
public interface RecipeTabComponent {

    void inject(RecipeTabActivity recipeTabActivity);

    void inject(RecipeTabCategoryFragmentList recipeTabCategoryFragment);

    @Subcomponent.Builder
    interface Builder {
        RecipeTabComponent build();
    }

}
