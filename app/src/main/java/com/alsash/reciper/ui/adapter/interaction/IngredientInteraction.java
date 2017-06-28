package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Ingredient;

/**
 * An ingredient interactions
 */
public interface IngredientInteraction {

    void onRequestName(Ingredient ingredient);

    void onRequestFood(Ingredient ingredient);

    void onEditWeight(Ingredient ingredient, int weight);
}
