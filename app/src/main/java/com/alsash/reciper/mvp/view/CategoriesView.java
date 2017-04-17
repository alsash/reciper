package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Category;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface CategoriesView extends MvpView {
    void addCategories(List<Category> categories);

    void showCategories();
}
