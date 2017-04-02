package com.alsash.reciper.model.entity;

import java.util.List;

public interface Recipe extends BaseEntity {

    String getName();

    Category getCategory();

    Nutrition getNutrition();

    List<Label> getLabels();
}
