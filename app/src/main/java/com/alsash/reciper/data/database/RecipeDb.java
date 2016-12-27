package com.alsash.reciper.data.database;

import com.alsash.reciper.data.model.Recipe;

public class RecipeDb implements Recipe {
    private long id;
    private String name;

    public RecipeDb(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
