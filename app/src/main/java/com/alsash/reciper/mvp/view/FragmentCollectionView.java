package com.alsash.reciper.mvp.view;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

/**
 * A FragmentCollectionView interface, that holds views that represent collections of fragments.
 */
public interface FragmentCollectionView extends BaseView {

    void showCollections(Fragment[] collections);

    void hideBottomNavigation(boolean hide);

    void showBottomNavigation(@IdRes int itemId);

    void showCollection(int position);

    int shownCollection();
}
