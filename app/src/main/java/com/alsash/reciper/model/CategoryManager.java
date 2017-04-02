package com.alsash.reciper.model;

import com.alsash.reciper.model.database.CategoryDb;
import com.alsash.reciper.model.entity.Category;

public class CategoryManager extends BaseEntityManager<Category> {
    private static CategoryManager instance = new CategoryManager();

    private CategoryManager() {
        super(10);
        // Obtain all relations
        RecipeManager.getInstance().list();
    }

    public static CategoryManager getInstance() {
        return instance;
    }

    @Override
    protected Category newEntity() {
        int id = entities.size();
        CategoryDb category = new CategoryDb();
        category.setId(id);
        category.setName("Category # " + id);
        return category;
    }
}
