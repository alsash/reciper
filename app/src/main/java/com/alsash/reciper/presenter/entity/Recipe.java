package com.alsash.reciper.presenter.entity;

import com.alsash.reciper.presenter.relation.Nutrition;

import java.util.List;

public interface Recipe extends BaseEntity {

    String getName();

    Category getCategory();

    Nutrition getNutrition();

    List<Label> getLabels();
}
