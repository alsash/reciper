package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Ingredient;

import java.util.List;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsIngredientsView extends BaseView {

    void showIngredients(List<Ingredient> ingredients);

}
