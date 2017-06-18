package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.tab.SwipeTab;

/**
 * A BaseView interface, which can be attached to BasePresenter.
 */
public interface RecipeDetailsView extends BaseView {

    void setDetails(SwipeTab[] details);

    void showDetail(int position);

    void showTitle(String title);

    void showPhoto(Photo photo);

    int shownDetail();
}
