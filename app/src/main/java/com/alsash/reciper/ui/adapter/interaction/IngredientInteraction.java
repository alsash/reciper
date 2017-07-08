package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Ingredient;

/**
 * An ingredient interactions
 */
public interface IngredientInteraction {

    void onEditValues(Ingredient ingredient, String name, int weight);
}
