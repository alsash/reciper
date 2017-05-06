package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.List;

public interface RecipeTabCategoryView extends BaseView {

    void setCategories(List<Category> categories);

    void showCategories();

    void showRecipeDetails(Recipe recipe);

    void showLoading(Boolean isLoading);
}
