package com.alsash.reciper.mvp.model.entity;

/**
 * An Ingredient entity
 */
public interface Ingredient extends BaseEntity {

    String getName();

    double getWeight();

    String getWeightUnit();

    Food getFood();
}
