package com.alsash.reciper.mvp.model.derivative;

import com.alsash.reciper.logic.unit.EnergyUnit;
import com.alsash.reciper.logic.unit.WeightUnit;

/**
 * A Nutrient model - derivative from an other model (Recipe, food, etc)
 */
public class Nutrient {

    private int protein;
    private int fat;
    private int carbs;
    private int energy;
    private WeightUnit weightUnit;
    private EnergyUnit energyUnit;

    private Nutrient() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getProtein() {
        return protein;
    }

    public int getProteinPercent() {
        if (getWeight() == 0 || protein == 0) return 0;
        return Math.round(protein / getWeight()) * 100;
    }

    public int getFat() {
        return fat;
    }

    public int getFatPercent() {
        if (getWeight() == 0 || fat == 0) return 0;
        return Math.round(fat / getWeight()) * 100;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getCarbsPercent() {
        if (getWeight() == 0 || carbs == 0) return 0;
        return Math.round(carbs / getWeight()) * 100;
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

    private int getWeight() {
        return protein + fat + carbs;
    }

    public static class Builder {

        private Nutrient nutrient;

        private Builder() {
            nutrient = new Nutrient();
        }

        public Builder protein(double protein) {
            nutrient.protein = (int) Math.round(protein);
            return this;
        }

        public Builder fat(double fat) {
            nutrient.fat = (int) Math.round(fat);
            return this;
        }

        public Builder carbs(double carbs) {
            nutrient.carbs = (int) Math.round(carbs);
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
