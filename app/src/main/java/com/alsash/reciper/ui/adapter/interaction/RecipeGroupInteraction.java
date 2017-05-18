package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.BaseRecipeGroup;

/**
 * A Recipe Group view interaction listener
 */
public interface RecipeGroupInteraction {

    /**
     * Notify about scroll event.
     * If there is need to stop listening of the scroll, an receiver must return true.
     *
     * @param recipeGroup       - current recipe group in the Group List
     * @param lastVisibleRecipe - position of the recipe in the list
     * @return - true if there is need to stop listening for a next scroll event.
     */
    boolean onRecipesScroll(BaseRecipeGroup recipeGroup, int lastVisibleRecipe);

}
