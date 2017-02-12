package com.alsash.reciper.data;

import android.support.annotation.Nullable;

import com.alsash.reciper.data.database.RecipeDb;
import com.alsash.reciper.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    public static final long RECIPE_ID_UNKNOWN = -1;

    private static RecipeManager instance = new RecipeManager();

    private final List<Recipe> recipes;

    private RecipeManager() {
        this.recipes = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            recipes.add(new RecipeDb(i, "Recipe # " + i));
        }
    }

    public static RecipeManager getInstance() {
        return instance;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Get exist Recipe or create new
     *
     * @param id of the exist recipe
     * @return new or exist recipe
     */
    public Recipe getRecipe(long id) {
        Recipe recipe = existRecipe(id);
        if (recipe == null) {
            recipe = newRecipe();
        }
        return recipe;
    }

    public Recipe newRecipe() {
        long id = recipes.size();
        Recipe recipe = new RecipeDb(id, "Recipe # " + id);
        recipes.add(recipe);
        return recipe;
    }

    @Nullable
    public Recipe existRecipe(long id) {
        if (id == RECIPE_ID_UNKNOWN) return null;
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) return recipe;
        }
        return null;
    }
}
