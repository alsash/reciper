package com.alsash.reciper.mvp.model.entity;

/**
 * A Food entity
 */
public interface Food extends BaseEntity {

    String getName();

    double getProtein();

    double getFat();

    double getCarbs();

    String getWeightUnit();

    double getEnergy();

    String getEnergyUnit();

    Measure getMeasure();
}
