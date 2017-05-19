package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Category;

import java.util.List;

public interface RecipeTabCategoryView extends BaseView {

    void setCategories(List<Category> categories);

    void setCategoriesScroll(boolean scroll);

    void showCategoriesLoading(boolean loading);

    void setRecipesScroll(int categoryPosition, boolean scroll);

    void showRecipesLoading(int categoryPosition, boolean loading);
}
