package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Category;

import java.util.List;

public interface RecipeCategoriesView extends BaseRecipesView {
    void addCategories(List<Category> categories);
}
