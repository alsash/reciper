package com.alsash.reciper.mvp.presenter;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.view.FragmentCollectionView;

/**
 * A FragmentCollectionPresenter
 */
public class FragmentCollectionPresenter implements BasePresenter<FragmentCollectionView> {

    private int shownCollection;

    private Fragment[] collections;
    private boolean mainCollectionShowed;

    public FragmentCollectionPresenter() {
    }

    public FragmentCollectionPresenter setCollections(Fragment[] collections) {
        this.collections = collections;
        shownCollection = collections.length > 1 ? 1 : 0;
        detach();
        return this;
    }

    public boolean onBottomNavigationPressed(FragmentCollectionView view, @IdRes int itemId) {
        Integer position = convertIdToPosition(itemId);
        if (position == null) return false;
        view.showCollection(position);
        return true;
    }

    @Override
    public void attach(FragmentCollectionView view) {
        view.showCollections(collections);
    }

    @Override
    public void visible(FragmentCollectionView view) {
        if (mainCollectionShowed) return;
        if (collections.length > 1) {
            view.hideBottomNavigation(false);
            Integer itemId = convertPositionToId(shownCollection);
            if (itemId != null) view.showBottomNavigation(itemId);
        } else {
            view.hideBottomNavigation(true);
            view.showCollection(shownCollection);
        }
        mainCollectionShowed = true;
    }

    @Override
    public void invisible(FragmentCollectionView view) {
        shownCollection = view.shownCollection();
    }

    @Override
    public void refresh(FragmentCollectionView view) {
        detach();
        attach(view);
    }

    @Override
    public void detach() {
        mainCollectionShowed = false;
    }

    @IdRes
    @Nullable
    private Integer convertPositionToId(int position) {
        switch (position) {
            case 0:
                return R.id.navigation_recipe_categories;
            case 1:
                return R.id.navigation_recipe_grid;
            case 2:
                return R.id.navigation_recipe_labels;
            default:
                return null;
        }
    }

    @Nullable
    private Integer convertIdToPosition(@IdRes int id) {
        switch (id) {
            case R.id.navigation_recipe_categories:
                return 0;
            case R.id.navigation_recipe_grid:
                return 1;
            case R.id.navigation_recipe_labels:
                return 2;
            default:
                return null;
        }
    }
}
