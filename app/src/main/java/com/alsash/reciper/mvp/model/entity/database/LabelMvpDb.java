package com.alsash.reciper.mvp.model.entity.database;


import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.List;

/**
 * Label class that represents Label MVP interface with
 * delegation to Label entity stored in database
 */
public class LabelMvpDb implements Label {

    private final Long id;
    private final String name;
    private final List<Recipe> recipes;

    public LabelMvpDb(Long id, String name, List<Recipe> recipes) {
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
