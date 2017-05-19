package com.alsash.reciper.ui.adapter.interaction;

/**
 * A Recipe Group view interaction listener
 */
public interface RecipeGroupInteraction {

    /**
     * Notification about scroll event.
     *
     * @param recipeGroup       - current recipe group in the Group List
     * @param lastVisibleRecipe - position of the recipe in the list
     */
    void onRecipesScroll(int recipeGroupPosition, int lastVisibleRecipe);

    /**
     * Check if there is need to listening of the scroll.
     *
     * @return - true if there is need to listen scroll events. False otherwise.
     */
    boolean doRecipesScroll(int recipeGroupPosition);
}
