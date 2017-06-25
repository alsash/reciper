package com.alsash.reciper.mvp.model.derivative;

import com.alsash.reciper.logic.unit.EnergyUnit;
import com.alsash.reciper.logic.unit.WeightUnit;

/**
 * A Nutrient model - derivative from an other model (Recipe, food, etc)
 */
public class Nutrient {

    private float protein;
    private float fat;
    private float carbs;
    private int energy;
    private WeightUnit weightUnit;
    private EnergyUnit energyUnit;

    private Nutrient() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public float getProtein() {
        return protein;
    }

    public int getProteinPercent() {
        if (getWeight() == 0 || protein == 0) return 0;
        return Math.round((protein * 100) / getWeight());
    }

    public float getFat() {
        return fat;
    }

    public int getFatPercent() {
        if (getWeight() == 0 || fat == 0) return 0;
        return Math.round((fat * 100) / getWeight());
    }

    public float getCarbs() {
        return carbs;
    }

    public int getCarbsPercent() {
        if (getWeight() == 0 || carbs == 0) return 0;
        return Math.round((carbs * 100) / getWeight());
    }

    public int getEnergy() {
        return energy;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public EnergyUnit getEnergyUnit() {
        return energyUnit;
    }

    private float getWeight() {
        return protein + fat + carbs;
    }

    public static class Builder {

        private Nutrient nutrient;

        private Builder() {
            nutrient = new Nutrient();
        }

        public Builder protein(double protein) {
            nutrient.protein = (float) protein;
            return this;
        }

        public Builder fat(double fat) {
            nutrient.fat = (float) fat;
            return this;
        }

        public Builder carbs(double carbs) {
            nutrient.carbs = (float) carbs;
            return this;
        }

        public Builder energy(double energy) {
            nutrient.energy = (int) Math.round(energy);
            return this;
        }

        public Builder unit(WeightUnit unit) {
            nutrient.weightUnit = unit;
            return this;
        }

        public Builder unit(EnergyUnit unit) {
            nutrient.energyUnit = unit;
            return this;
        }

        public Nutrient build() {
            Nutrient nutrient = this.nutrient;
            this.nutrient = null;
            return nutrient;
        }
    }
}
