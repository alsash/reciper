package com.alsash.reciper.logic;

import android.support.annotation.Nullable;
import android.util.Log;

import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.exception.UnitException;
import com.alsash.reciper.logic.unit.EnergyUnit;
import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.logic.unit.WeightUnit;
import com.alsash.reciper.mvp.model.derivative.Nutrient;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;

import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * A Business Logic. Makes business processes clear for understanding and usage.
 */
public class BusinessLogic {

    private static final String TAG = "BusinessLogic";
    private final Subject<RecipeEvent> recipeEventSubject;
    private final Comparator<Recipe> recipeComparator;

    public BusinessLogic() {
        recipeEventSubject = BehaviorSubject.create();
        recipeEventSubject.subscribeOn(AndroidSchedulers.mainThread());
        recipeComparator = new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                if (r1.isFavorite() != r2.isFavorite())
                    return (r1.isFavorite() ? 1 : -1);
                return r1.getName().compareTo(r2.getName());
            }
        };
    }

    public Nutrient getNutrient(RecipeFull recipe, RecipeUnit recipeUnit) {
        double protein = 0.0D;
        double carbs = 0.0D;
        double fat = 0.0D;
        double energy = 0.0D;
        try {
            double recipeProteinInGrams = 0.0D;
            double recipeCarbsInGrams = 0.0D;
            double recipeFatInGrams = 0.0D;
            double recipeEnergyInKiloCalories = 0.0D;
            double recipeWeightInGrams = 0.0D;
            for (Ingredient ingredient : recipe.getIngredients()) {
                // Getting weight multiplier
                WeightUnit ingredientWeightUnit = WeightUnit.getValueOf(ingredient.getWeightUnit());
                if (ingredientWeightUnit == null)
                    throw new UnitException(ingredient, "weightUnit", ingredient.getWeightUnit());

                Food food = ingredient.getFood();
                WeightUnit foodWeightUnit = WeightUnit.getValueOf(food.getWeightUnit());
                if (foodWeightUnit == null)
                    throw new UnitException(food, "weightUnit", food.getWeightUnit());

                double ingredientFoodWeightMultiplier = getWeightMultiplier(WeightUnit.GRAM,
                        ingredientWeightUnit, foodWeightUnit);

                // Calculating nutrition values - food contains value per 1 unit
                recipeProteinInGrams += food.getProtein() * ingredient.getWeight() *
                        ingredientFoodWeightMultiplier;
                recipeCarbsInGrams += food.getCarbs() * ingredient.getWeight() *
                        ingredientFoodWeightMultiplier;
                recipeFatInGrams += food.getFat() * ingredient.getWeight() *
                        ingredientFoodWeightMultiplier;

                // Getting energy per weight multiplier
                EnergyUnit foodEnergyUnit = EnergyUnit.getValueOf(food.getEnergyUnit());
                if (foodEnergyUnit == null)
                    throw new UnitException(food, "energyUnit", food.getEnergyUnit());

                double energyWeightMultiplier = getEnergyMultiplier(EnergyUnit.CALORIE,
                        foodEnergyUnit) * getWeightMultiplier(WeightUnit.GRAM,
                        ingredientWeightUnit);

                // Calculating energy value
                recipeEnergyInKiloCalories += food.getEnergy() * ingredient.getWeight() *
                        energyWeightMultiplier;

                // Calculating recipe weight
                recipeWeightInGrams += ingredient.getWeight() *
                        getWeightMultiplier(WeightUnit.GRAM, ingredientWeightUnit);

            }

            // Calculating values per RecipeUnit
            double nutrientMultiplier = recipeUnit.getDefaultQuantity();
            switch (recipeUnit) {
                case GRAM:
                    nutrientMultiplier /= recipeWeightInGrams;
                    break;
                case SERVING:
                    nutrientMultiplier /= recipeWeightInGrams / recipe.getServings();
            }
            protein = recipeProteinInGrams * nutrientMultiplier;
            carbs = recipeCarbsInGrams * nutrientMultiplier;
            fat = recipeFatInGrams * nutrientMultiplier;
            energy = recipeEnergyInKiloCalories * nutrientMultiplier;

        } catch (Throwable e) {
            Log.d(TAG, e.getMessage(), e);
        }

        return Nutrient.builder()
                .protein(protein)
                .carbs(carbs)
                .fat(fat)
                .unit(WeightUnit.GRAM)
                .energy(energy)
                .unit(EnergyUnit.CALORIE)
                .build();
    }

    private double getWeightMultiplier(WeightUnit requiredUnit, WeightUnit... units) {
        double multiplier = 1.0D;
        for (WeightUnit unit : units) {
            switch (requiredUnit) {
                case GRAM:
                    switch (unit) {
                        case GRAM:
                            break;
                        case KILOGRAM:
                            multiplier *= 1000.0D;
                            break;
                    }
                    break;
                case KILOGRAM:
                    switch (unit) {
                        case GRAM:
                            multiplier /= 1000.0D;
                            break;
                        case KILOGRAM:
                            break;
                    }
                    break;
            }
        }
        return multiplier;
    }

    private double getEnergyMultiplier(EnergyUnit requiredUnit, EnergyUnit... units) {
        double multiplier = 1.0D;
        for (EnergyUnit unit : units) {
            switch (requiredUnit) {
                case CALORIE:
                    switch (unit) {
                        case CALORIE:
                            break;
                        case KILOCALORIE:
                            multiplier *= 1000.0D;
                            break;
                    }
                    break;
                case KILOCALORIE:
                    switch (unit) {
                        case CALORIE:
                            multiplier /= 1000.0D;
                        case KILOCALORIE:
                            break;
                    }
                    break;
            }
        }
        return multiplier;
    }

    public Subject<RecipeEvent> getRecipeEventSubject() {
        return recipeEventSubject;
    }

    /**
     * Move a Recipe from one position to another
     *
     * @param recipes      - list of recipes
     * @param fromPosition - position of a recipe before moving
     * @return - position of a recipe after moving or null
     */
    @Nullable
    public Integer moveToStart(List<Recipe> recipes, int fromPosition) {
        if (recipes == null || recipes.size() <= fromPosition) return null;
        for (int i = 0; i < fromPosition; i++) {
            if (recipeComparator.compare(recipes.get(i), recipes.get(fromPosition)) <= 0) {
                Recipe recipe = recipes.set(i, recipes.remove(fromPosition));
                for (int j = i + 1; j < recipes.size(); j++) {
                    recipe = recipes.set(j, recipe);
                }
                recipes.add(recipe);
                return i;
            }
        }
        return null;
    }

}
