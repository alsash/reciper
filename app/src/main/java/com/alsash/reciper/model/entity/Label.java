package com.alsash.reciper.model.entity;

import java.util.List;

public interface Label extends BaseEntity {

    String getName();

    List<Recipe> getRecipes();
}
