package com.alsash.reciper.mvp.model.entity;

import android.support.annotation.Nullable;

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

    @Nullable
    String getNdbNo();

    @Nullable
    Measure getMeasure();
}
