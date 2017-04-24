package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Recipe;

public interface BaseRecipesView {

    void showDetails(Recipe recipe);

    void showRecipe(Recipe recipe);
}
