package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Recipe;

/**
 * A Recipe view interaction listener
 */
public interface RecipeSingleInteraction {

    void onRecipeExpand(Recipe recipe);

    void onRecipeOpen(Recipe recipe);
}
