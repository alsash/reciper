package com.alsash.reciper.ui.interaction;

import com.alsash.reciper.data.model.Recipe;

public interface RecipeListInteraction {

    void onExpand(Recipe recipe, int position);

    void onOpen(Recipe recipe, int position);
}
