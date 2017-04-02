package com.alsash.reciper.model.database;

import com.alsash.reciper.presenter.entity.Category;
import com.alsash.reciper.presenter.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CategoryDb implements Category {

    private long id;
    private String name;
    private List<Recipe> recipes = new ArrayList<>();

    public CategoryDb() {
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
