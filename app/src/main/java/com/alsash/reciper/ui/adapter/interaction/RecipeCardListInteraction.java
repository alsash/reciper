package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Recipe;

/**
 * A Recipe view interaction listener
 */
public interface RecipeCardListInteraction {

    void onExpand(Recipe recipe);

    void onOpen(Recipe recipe);
}
