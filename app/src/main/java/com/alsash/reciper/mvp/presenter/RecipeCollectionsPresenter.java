package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.mvp.view.RecipeCollectionsView;
import com.alsash.reciper.ui.fragment.RecipeCollectionCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionGridFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionLabelFragment;

/**
 * A RecipeCollectionsPresenter
 */
public class RecipeCollectionsPresenter implements BasePresenter<RecipeCollectionsView> {

    private static final int MAIN_COLLECTION_POSITION = 1;

    private final Fragment[] collections = new Fragment[]{
            RecipeCollectionCategoryFragment.newInstance(),
            RecipeCollectionGridFragment.newInstance(),
            RecipeCollectionLabelFragment.newInstance()
    };

    private boolean mainCollectionShowed;

    @Override
    public void attach(RecipeCollectionsView view) {
        view.setCollections(collections);
    }

    @Override
    public void visible(RecipeCollectionsView view) {
        if (mainCollectionShowed) return;
        view.showCollection(MAIN_COLLECTION_POSITION);
        mainCollectionShowed = true;
    }

    @Override
    public void invisible(RecipeCollectionsView view) {
        // Do nothing
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
