package com.alsash.reciper.model;

import android.support.annotation.Nullable;

import com.alsash.reciper.model.database.RecipeDb;
import com.alsash.reciper.model.models.Category;
import com.alsash.reciper.model.models.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManager {

    private static RecipeManager instance = new RecipeManager();

    private final List<Recipe> recipes;

    private final Map<Category, List<Recipe>> categories;

    private RecipeManager() {
        this.recipes = new ArrayList<>();
        this.categories = new HashMap<>();
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

    public List<Recipe> getRecipes(Category category) {
        if (categories.containsKey(category)) {
            return categories.get(category);
        }
        List<Recipe> categoryRecipes = new ArrayList<>();
        for (int i = 0; i < category.getId(); i++) {
            Recipe recipe = recipes.get(i);
            recipe.setCategory(category);
            categoryRecipes.add(recipe);
        }
        categories.put(category, categoryRecipes);
        return categoryRecipes;
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
