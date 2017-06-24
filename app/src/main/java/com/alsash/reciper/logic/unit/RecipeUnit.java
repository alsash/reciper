package com.alsash.reciper.logic.unit;

/**
 * Unit, that represent quantity of an recipe
 */

public enum RecipeUnit {
    GRAM(100), SERVING(1);

    private final int defaultQuantity;

    RecipeUnit(int defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
    }

    public int getDefaultQuantity() {
        return defaultQuantity;
    }
}
