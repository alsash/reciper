package com.alsash.reciper.model;

import android.support.annotation.Nullable;

import com.alsash.reciper.model.database.CategoryDb;
import com.alsash.reciper.model.entity.Category;
import com.alsash.reciper.model.entity.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryManager {
    private static CategoryManager instance = new CategoryManager();

    private List<Category> categories;
    private Map<Category, List<Recipe>> categoryRecipesMap = new HashMap<>();

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

    public Map<Category, List<Recipe>> getCategoryRecipesMap() {
        if (categoryRecipesMap.size() == 0) {
            for (Category category : categories) {
                categoryRecipesMap.put(category, RecipeManager.getInstance().getRecipes(category));
            }
        }
        return categoryRecipesMap;
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
