package com.alsash.reciper.data.database;

import com.alsash.reciper.data.model.Nutrition;
import com.alsash.reciper.data.model.Recipe;

public class RecipeDb implements Recipe, Nutrition {
    private long id;
    private String name;

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
        return 70;
    }

    @Override
    public int getProtein() {
        return 20;
    }

    @Override
    public int getFat() {
        return 10;
    }

    @Override
    public int getEnergy() {
        return 200;
    }
}
