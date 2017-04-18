package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface BaseRecipesView extends MvpView {

    void showContent();

    void showDetails(Recipe recipe);

    void showRecipe(Recipe recipe);
}
