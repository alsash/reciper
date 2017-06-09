package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeTabModule;
import com.alsash.reciper.di.scope.RecipeTabScope;
import com.alsash.reciper.ui.activity.RecipeTabActivity;
import com.alsash.reciper.ui.fragment.RecipeCollectionGridFragment;
import com.alsash.reciper.ui.fragment.RecipeTabBookmarkFragment;
import com.alsash.reciper.ui.fragment.RecipeTabCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeTabLabelFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between recipe tab views and theirs dependencies
 */
@RecipeTabScope
@Subcomponent(modules = {UiRecipeTabModule.class})
public interface UiRecipeTabComponent {

    void inject(RecipeTabActivity recipeTabActivity);

    void inject(RecipeTabCategoryFragment recipeTabCategoryFragment);

    void inject(RecipeCollectionGridFragment recipeTabGridFragment);

    void inject(RecipeTabLabelFragment recipeTabLabelFragment);

    void inject(RecipeTabBookmarkFragment recipeTabBookmarkFragment);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeTabComponent build();
    }

}
