package com.alsash.reciper.mvp.view;

import com.alsash.reciper.app.lib.MutableLong;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.List;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsDescriptionView extends BaseView {

    void showAuthor(Author author);

    void showDescription(Recipe recipe);

    String[] getDescriptionEditable();

    void setDescriptionEditable(boolean editable);

    void showCategory(Category category);

    void showCategoryEditDialog(Recipe recipe);

    void showAuthorEditDialog(Recipe recipe);

    void showLabels(List<Label> labels);

    void showLabelEditDialog(Recipe recipe);

    void showCookTime(long millis);

    void showCookTimeEditDialog(MutableLong listener);

}
