package com.alsash.reciper.model.database;

import com.alsash.reciper.model.entity.Category;
import com.alsash.reciper.model.entity.Label;
import com.alsash.reciper.model.entity.Nutrition;
import com.alsash.reciper.model.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDb implements Recipe, Nutrition {
    private long id;
    private String name;
    private Category category;
    private List<Label> labels = new ArrayList<>();

    public RecipeDb() {
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
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
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

    public static class Builder {
        private long id;
        private String name;
        private Category category;
    }

}
