package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.mvp.view.RecipeCollectionsView;

/**
 * A RecipeCollectionsPresenter
 */
public class RecipeCollectionsPresenter implements BasePresenter<RecipeCollectionsView> {

    private int shownCollection;

    private Fragment[] collections;
    private boolean mainCollectionShowed;

    public RecipeCollectionsPresenter() {
    }

    public RecipeCollectionsPresenter setCollections(Fragment[] collections) {
        this.collections = collections;
        if (collections.length > 0) shownCollection = 1;
        return this;
    }

    @Override
    public void attach(RecipeCollectionsView view) {
        view.setCollections(collections);
    }

    @Override
    public void visible(RecipeCollectionsView view) {
        if (mainCollectionShowed) return;
        view.showCollection(shownCollection);
        mainCollectionShowed = true;
    }

    @Override
    public void invisible(RecipeCollectionsView view) {
        shownCollection = view.shownCollection();
    }

    @Override
    public void refresh(RecipeCollectionsView view) {
        detach();
    }

    @Override
    public void detach() {
        mainCollectionShowed = false;
    }
}
