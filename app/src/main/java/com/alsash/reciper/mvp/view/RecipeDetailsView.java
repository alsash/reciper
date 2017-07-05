package com.alsash.reciper.mvp.view;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.tab.SwipeTab;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsView extends BaseView {

    void setDetails(SwipeTab[] details);

    void showDetail(int position);

    int shownDetail();

    void showTitle(String title);

    void showNameEditDialog(Recipe recipe, MutableString listener);

    void showPhoto(Photo photo);

    void showPhotoEditDialog(Photo photo, MutableString listener);

    void showConfirmDeleteDialog(MutableBoolean listener);

    void finishView();
}
