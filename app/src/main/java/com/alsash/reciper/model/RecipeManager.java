package com.alsash.reciper.model;

import android.support.annotation.Nullable;

import com.alsash.reciper.model.database.RecipeDb;
import com.alsash.reciper.model.entity.Recipe;

public class RecipeManager extends EntityManager<Recipe> {

    private static RecipeManager instance = new RecipeManager();

    private RecipeManager() {
        super(30);
    }

    public static RecipeManager getInstance() {
        return instance;
    }


    @Override
    public Recipe newEntity() {
        return null;
    }

    @Override
    public Recipe getEntity(long id) {
        return null;
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
