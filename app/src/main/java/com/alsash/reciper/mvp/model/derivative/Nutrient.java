package com.alsash.reciper.mvp.model.derivative;

/**
 * A Nutrient model
 */
public interface Nutrient {

    double getProtein();

    double getFat();

    double getCarbs();

    String getWeightUnit();

    double getEnergy();

    String getEnergyUnit();

}
