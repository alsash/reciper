package com.alsash.reciper.model.models;

public interface Recipe {
    long getId();

    String getName();

    Nutrition getNutrition();

    Category getCategory();

    void setCategory(Category category);
}
