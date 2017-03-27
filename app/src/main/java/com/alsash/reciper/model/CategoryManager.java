package com.alsash.reciper.model;

import com.alsash.reciper.model.database.CategoryDb;
import com.alsash.reciper.model.entity.Category;

public class CategoryManager extends BaseEntityManager<Category, Long> {
    private static CategoryManager instance = new CategoryManager();

    private CategoryManager() {
        super(20);
    }

    public static CategoryManager getInstance() {
        return instance;
    }

    @Override
    protected Long getKey(Category category) {
        return category.getId();
    }

    @Override
    protected Category newEntity() {
        long id = entities.size();
        return new CategoryDb(id, "Category # " + id);
    }
}
