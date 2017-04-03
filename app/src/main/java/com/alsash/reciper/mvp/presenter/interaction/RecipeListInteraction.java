package com.alsash.reciper.mvp.presenter.interaction;

import com.alsash.reciper.mvp.model.Recipe;

/**
 * Created by aldolyna on 4/3/17.
 */
public interface RecipeListInteraction {
    void onExpand(Recipe recipe, int position);

    void onOpen(Recipe recipe, int position);
}
