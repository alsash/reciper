package com.alsash.reciper.model;

import com.alsash.reciper.model.database.RecipeDb;
import com.alsash.reciper.model.entity.Recipe;

public class RecipeManager extends BaseEntityManager<Recipe, Long> {

    private static RecipeManager instance = new RecipeManager();

    private RecipeManager() {
        super(30);
    }

    public static RecipeManager getInstance() {
        return instance;
    }

    @Override
    protected Long getKey(Recipe recipe) {
        return recipe.getId();
    }

    @Override
    protected Recipe newEntity() {
        long id = entities.size();
        return new RecipeDb(id, "Recipe # " + id);
    }
}
