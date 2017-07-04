package com.alsash.reciper.mvp.view;

import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.derivative.Nutrient;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsNutritionView extends BaseView {

    void showNutritionQuantity(int quantity, RecipeUnit recipeUnit);

    void showNutritionChart(Nutrient nutrient);

    void showNutritionAnimation();

}
