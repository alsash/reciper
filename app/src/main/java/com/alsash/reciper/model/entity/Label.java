package com.alsash.reciper.model.entity;

import java.util.List;

public interface Label extends Entity {

    String getName();

    List<Recipe> getRecipes();
}
