package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.tab.SwipeTab;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsView extends BaseView {

    void setDetails(SwipeTab[] details);

    void showDetail(int position);

    int shownDetail();

    void showTitle(String title);

    void showPhoto(Photo photo);
}
