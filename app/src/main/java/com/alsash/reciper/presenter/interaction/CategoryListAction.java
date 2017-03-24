package com.alsash.reciper.presenter.interaction;

import com.alsash.reciper.model.models.Category;

public interface CategoryListAction {
    void onTitleVisible(Category category, int position);

    void onTitleGone(Category category, int position);
}
