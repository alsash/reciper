package com.alsash.reciper.mvp.presenter.interaction;

import com.alsash.reciper.mvp.model.entity.Recipe;

/**
 * An Recipe list view interaction listener
 */
public interface RecipeListInteraction {
    void onExpand(Recipe recipe, int position);

    void onOpen(Recipe recipe, int position);
}
