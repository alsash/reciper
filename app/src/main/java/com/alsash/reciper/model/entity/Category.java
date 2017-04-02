package com.alsash.reciper.model.entity;

import java.util.List;

public interface Category extends BaseEntity {

    String getName();

    List<Recipe> getRecipes();
}
