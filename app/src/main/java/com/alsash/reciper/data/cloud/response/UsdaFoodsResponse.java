package com.alsash.reciper.data.cloud.response;

import java.util.List;

/**
 * An Response from Usda public API
 */
public class UsdaFoodsResponse {

    public List<FoodContainer> foods;
    public int count;
    public int notfound;
    public int api;

    public static class FoodContainer {
        public Food food;
    }

    public static class Food {
        public String sr;
        public String type;
        public Desc desc;
        public List<Nutrient> nutrients;
    }

    public static class Desc {
        public String ndbno;
        public String name;
        public String ds;
        public String ru;
    }

    public static class Nutrient {
        public int nutrientId;
        public String name;
        public String group;
        public String unit;
        public double value; // 100 g equivalent value of the nutrient
        public List<Measure> measures;
    }

    public static class Measure {
        public String label;
        public int eqv;
        public String eunit;
        public int qty;
        public String value;
    }
}
