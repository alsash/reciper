package com.alsash.reciper.data;

import com.alsash.reciper.data.database.RecipeDb;
import com.alsash.reciper.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    private static RecipeManager instance = new RecipeManager();

    public static RecipeManager getInstance() {
        return instance;
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            recipes.add(new RecipeDb(i, "Recipe # " + i));
        }
        return recipes;
    }

}
