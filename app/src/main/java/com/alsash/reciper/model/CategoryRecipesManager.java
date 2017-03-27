package com.alsash.reciper.model;

public class CategoryRecipesManager {
    private static final CategoryRecipesManager instance = new CategoryRecipesManager();

    private CategoryRecipesManager() {

    }

    public static CategoryRecipesManager getInstance() {
        return instance;
    }


}
