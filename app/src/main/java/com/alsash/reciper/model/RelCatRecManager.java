package com.alsash.reciper.model;

import com.alsash.reciper.model.entity.Category;
import com.alsash.reciper.model.entity.Recipe;

public class RelCatRecManager extends BaseEntityManager<Recipe, Category> {
    private static final RelCatRecManager instance = new RelCatRecManager();

    private RelCatRecManager() {
        super(CategoryManager.getInstance().list().size());
    }

    public static RelCatRecManager getInstance() {
        return instance;
    }

    @Override
    protected Category getKey(Recipe entity) {
        return null;
    }

    @Override
    protected Recipe newEntity() {
        return null;
    }
}
