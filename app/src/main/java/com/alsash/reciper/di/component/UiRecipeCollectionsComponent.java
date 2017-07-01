package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiRecipeCollectionsModule;
import com.alsash.reciper.di.scope.RecipeCollectionsScope;
import com.alsash.reciper.ui.fragment.RecipeCollectionCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionGridFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionLabelFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between recipe collection views and theirs dependencies
 */
@RecipeCollectionsScope
@Subcomponent(
        modules = {
                UiRecipeCollectionsModule.class
        }
)
public interface UiRecipeCollectionsComponent {

    void inject(RecipeCollectionCategoryFragment recipeCollectionCategoryFragment);

    void inject(RecipeCollectionGridFragment recipeCollectionGridFragment);

    void inject(RecipeCollectionLabelFragment recipeCollectionLabelFragment);

    @Subcomponent.Builder
    interface Builder {
        UiRecipeCollectionsComponent build();
    }
}
