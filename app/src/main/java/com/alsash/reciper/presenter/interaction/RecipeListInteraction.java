package com.alsash.reciper.presenter.interaction;

import com.alsash.reciper.model.entity.Recipe;

public interface RecipeListInteraction {

    void onExpand(Recipe recipe, int position);

    void onOpen(Recipe recipe, int position);
}
