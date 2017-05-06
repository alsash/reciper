package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Recipe;

/**
 * A Recipe list view interaction listener
 */
public interface RecipeListInteraction {

    void onExpand(Recipe recipe);

    void onOpen(Recipe recipe);
}
