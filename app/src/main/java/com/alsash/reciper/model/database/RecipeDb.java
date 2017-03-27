package com.alsash.reciper.model.database;

import com.alsash.reciper.model.entity.Category;
import com.alsash.reciper.model.entity.Nutrition;
import com.alsash.reciper.model.entity.Recipe;

public class RecipeDb implements Recipe, Nutrition {
    private long id;
    private String name;
    private Category category;

    public RecipeDb(long id, String name) {
        this.id = id;
        this.name = name;
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
    public Nutrition getNutrition() {
        return this;
    }

    @Override
    public int getCarbohydrate() {
        return 50;
    }

    @Override
    public int getProtein() {
        return 100;
    }

    @Override
    public int getFat() {
        return 30;
    }

    @Override
    public int getEnergy() {
        return 200;
    }
}
