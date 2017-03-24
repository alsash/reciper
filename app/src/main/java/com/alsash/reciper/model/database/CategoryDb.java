package com.alsash.reciper.model.database;

import com.alsash.reciper.model.models.Category;

import java.util.List;

public class CategoryDb implements Category {

    private long id;

    private String name;

    private List<Long> recipeIds;

    public CategoryDb(long id, String name, List<Long> recipeIds) {
        this.id = id;
        this.name = name;
        this.recipeIds = recipeIds;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Long> getRecipeIds() {
        return recipeIds;
    }
}
