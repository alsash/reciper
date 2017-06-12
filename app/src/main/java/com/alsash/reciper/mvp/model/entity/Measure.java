package com.alsash.reciper.mvp.model.entity;

/**
 * A Measure model (Food-measure)
 */
public interface Measure extends BaseEntity {

    String getUnitOne();

    String getUnitOther();

    double getWeight();

    String getWeightUnit();
}
