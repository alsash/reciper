package com.alsash.reciper.logic;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.mvp.model.derivative.Nutrient;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Application business logic.
 * Makes business processes clear for understanding and usage.
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

    public Nutrient getNutrient(Recipe recipe) {
        return null;
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
