package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Category;

/**
 * Simple list view
 */
public interface RecipeCategoryDialogView extends BaseListView<Category> {

    void setSelected(Category category);

    void finishView();
}
