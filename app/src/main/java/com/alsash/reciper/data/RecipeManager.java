package com.alsash.reciper.data;

import com.alsash.reciper.data.database.RecipeDb;
import com.alsash.reciper.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

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

}
