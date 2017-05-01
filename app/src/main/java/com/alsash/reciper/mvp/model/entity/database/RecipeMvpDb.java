package com.alsash.reciper.mvp.model.entity.database;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.List;

/**
 * RecipeTable class that represents RecipeTable MVP interface with
 * delegation to RecipeTable entity stored in database
 */
public class RecipeMvpDb implements Recipe {

    private final Long id;
    private final String name;
    private final Category category;
    private final List<Label> labels;

    public RecipeMvpDb(Long id, String name, Category category, List<Label> labels) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.labels = labels;
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
    public Category getCategory() {
        return category;
    }

    @Override
    public List<Label> getLabels() {
        return labels;
    }
}
