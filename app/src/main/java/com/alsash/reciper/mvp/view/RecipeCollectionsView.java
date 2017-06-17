package com.alsash.reciper.mvp.view;

import android.support.v4.app.Fragment;

/**
 * A RecipeCollectionsView interface, that holds views that represent collections of recipes.
 */
public interface RecipeCollectionsView extends BaseView {

    void setCollections(Fragment[] collections);

    void showCollection(int position);

    int shownCollection();
}
