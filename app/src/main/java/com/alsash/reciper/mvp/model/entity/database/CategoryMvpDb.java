package com.alsash.reciper.mvp.model.entity.database;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Category class that represents Category MVP interface with
 * delegation to Category entity stored in database
 */
public class CategoryMvpDb implements Category {

    private final com.alsash.reciper.database.entity.Category categoryDb;
    private final List<Recipe> categoryRecipes;

    public CategoryMvpDb(com.alsash.reciper.database.entity.Category categoryDb) {
        this.categoryDb = categoryDb;
        // Fetch items from database
        this.categoryRecipes = getRecipes();
    }

    @Override
    public Long getId() {
        return categoryDb.getId();
    }

    @Override
    public String getName() {
        return categoryDb.getName();
    }

    @Override
    public List<Recipe> getRecipes() {
        if (categoryRecipes != null) return categoryRecipes;
        List<Recipe> recipes = new ArrayList<>();
        for (com.alsash.reciper.database.entity.Recipe recipe : categoryDb.getRecipes()) {
            recipes.add(new RecipeMvpDb(recipe));
        }
        return recipes;
    }
}
