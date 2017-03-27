package com.alsash.reciper.model.database;

import android.support.annotation.Nullable;

import com.alsash.reciper.model.entity.Category;
import com.alsash.reciper.model.entity.Recipe;
import com.alsash.reciper.model.relation.Relations;

import java.util.Collections;
import java.util.List;

public class RelCatRecDb implements Relations<Category, Recipe> {

    private final Category category;
    private final List<Recipe> recipes;

    public RelCatRecDb(Category category, List<Recipe> recipes) {
        this.category = category;
        this.recipes = Collections.unmodifiableList(recipes);
    }

    @Override
    public Category getObject() {
        return category;
    }

    @Nullable
    @Override
    public Recipe getSubject() {
        return recipes.size() == 0 ? null : recipes.get(0);
    }

    @Override
    public List<Recipe> getSubjects() {
        return recipes;
    }
}
