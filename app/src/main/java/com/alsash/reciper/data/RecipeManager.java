package com.alsash.reciper.data;

import android.support.annotation.Nullable;

import com.alsash.reciper.data.database.RecipeDb;
import com.alsash.reciper.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    private static RecipeManager instance = new RecipeManager();

    private final List<Recipe> recipes;

    private RecipeManager() {
        this.recipes = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            recipes.add(new RecipeDb(i, "Recipe # " + i));
        }
    }

    public static RecipeManager getInstance() {
        return instance;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Recipe newRecipe() {
        long id = recipes.size();
        Recipe recipe = new RecipeDb(id, "Recipe # " + id);
        recipes.add(recipe);
        return recipe;
    }

    @Nullable
    public Recipe getRecipe(long id) {
        if (id < 0) return null;
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) return recipe;
        }
        return null;
    }
}
