package com.alsash.reciper.mvp.view;

import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.derivative.Nutrient;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.Calendar;
import java.util.List;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsDescriptionView extends BaseView {

    void showDescription(Recipe recipe);

    void showCategory(Category category);

    void showLabels(List<Label> labels);

    void showCookTime(Calendar calendar);

    void showNutritionQuantity(int quantity, RecipeUnit recipeUnit);

    void showNutritionChart(Nutrient nutrient);

    void showNutritionAnimation();

}
