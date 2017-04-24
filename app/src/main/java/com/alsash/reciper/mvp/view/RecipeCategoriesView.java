package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Category;

import java.util.List;

public interface RecipeCategoriesView extends BaseRecipesView {

    void setCategories(List<Category> categories);

    void addCategories(List<Category> categories);

    void showCategories();
}
