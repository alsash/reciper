package com.alsash.reciper.mvp.view;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.unit.WeightUnit;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.List;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsIngredientsView extends BaseView {

    void showWeight(int weight, WeightUnit unit);

    void showIngredients(List<Ingredient> ingredients);

    void showIngredientsAddDialog(Recipe recipe);

    void showIngredientInsert(int position);

    void showIngredientDelete(int position);

    void showIngredientDeleteMessage(String ingredientName, final MutableBoolean rejectCallback);
}
