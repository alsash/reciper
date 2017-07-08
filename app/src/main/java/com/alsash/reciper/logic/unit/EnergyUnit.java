package com.alsash.reciper.logic.unit;

import android.support.annotation.Nullable;

/**
 * Energy units, that present in an application
 */
public enum EnergyUnit {

    CALORIE("cal"), KILOCALORIE("kCal");

    private final String unitString;

    EnergyUnit(String unitString) {
        this.unitString = unitString;
    }

    @Nullable
    public static EnergyUnit getValueOf(String name) {
        if (name == null) return null;
        if (name.toLowerCase().equals("cal")) return CALORIE;
        if (name.toLowerCase().equals("kcal")) return KILOCALORIE;
        return null;
    }

    @Override
    public String toString() {
        return unitString;
    }
}
