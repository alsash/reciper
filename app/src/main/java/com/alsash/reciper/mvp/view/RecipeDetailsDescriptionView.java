package com.alsash.reciper.mvp.view;

import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.derivative.Nutrient;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsDescriptionView extends BaseView {

    void showNutritionSwitch(int quantity, RecipeUnit recipeUnit);

    void showNutritionValue(Nutrient nutrient, boolean animated);

}
