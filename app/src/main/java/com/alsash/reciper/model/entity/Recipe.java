package com.alsash.reciper.model.entity;

import java.util.List;

public interface Recipe extends Entity {

    String getName();

    Category getCategory();

    Nutrition getNutrition();

    List<Label> getLabels();
}
