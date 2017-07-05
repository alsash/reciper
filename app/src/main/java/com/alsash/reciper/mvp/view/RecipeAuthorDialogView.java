package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Author;

/**
 * Simple list view
 */
public interface RecipeAuthorDialogView extends BaseListView<Author> {

    void setSelected(Author author);

    void finishView();
}
