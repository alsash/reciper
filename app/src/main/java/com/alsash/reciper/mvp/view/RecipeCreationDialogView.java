package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;

/**
 * Composite list view
 */
public interface RecipeCreationDialogView extends BaseView {

    BaseListView<Category> getCategoriesView();

    BaseListView<Author> getAuthorsView();

    void openRecipe(Recipe recipe);

    void showError();

    void finishView();
}
