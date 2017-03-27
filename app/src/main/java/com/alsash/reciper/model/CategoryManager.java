package com.alsash.reciper.model;

import android.support.annotation.Nullable;

import com.alsash.reciper.model.database.CategoryDb;
import com.alsash.reciper.model.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private static CategoryManager instance = new CategoryManager();

    private List<Category> categories;

    private CategoryManager() {
        categories = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            newCategory();
        }
    }

    public static CategoryManager getInstance() {
        return instance;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category newCategory() {
        long id = categories.size();
        Category category = new CategoryDb(id, "Category # " + id);
        categories.add(category);
        return category;
    }

    @Nullable
    public Category getCategory(long id) {
        if (id < 0) return null;
        for (Category category : categories) {
            if (category.getId() == id) return category;
        }
        return null;
    }
}
