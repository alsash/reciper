package com.alsash.reciper.logic.unit;

import android.support.annotation.Nullable;

/**
 * Energy units, that present in an application
 */
public enum WeightUnit {

    GRAM("g"), KILOGRAM("kg");

    private final String unitString;

    WeightUnit(String unitString) {
        this.unitString = unitString;
    }

    @Nullable
    public static WeightUnit getValueOf(String name) {
        if (name == null) return null;
        if (name.toLowerCase().equals("g")) return GRAM;
        if (name.toLowerCase().equals("kg")) return KILOGRAM;
        return null;
    }

    @Override
    public String toString() {
        return unitString;
    }
}
