package com.alsash.reciper.mvp.model.entity.database;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.List;

/**
 * CategoryTable class that represents CategoryTable MVP interface with
 * delegation to CategoryTable entity stored in database
 */
public class CategoryMvpDb implements Category {

    private final Long id;
    private final String name;
    private final List<Recipe> recipes;

    public CategoryMvpDb(Long id, String name, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.recipes = recipes;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipes;
    }
}
