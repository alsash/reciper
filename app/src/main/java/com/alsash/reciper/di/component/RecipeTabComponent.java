package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.RecipeTabModule;
import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.ui.activity.RecipeTabActivity;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between RecipeTabActivity, its fragments and theirs dependencies
 */
@StartScope
@Subcomponent(modules = {RecipeTabModule.class})
public interface RecipeTabComponent {

    void inject(RecipeTabActivity recipeTabActivity);

    @Subcomponent.Builder
    interface Builder {
        RecipeTabComponent build();
    }

}
