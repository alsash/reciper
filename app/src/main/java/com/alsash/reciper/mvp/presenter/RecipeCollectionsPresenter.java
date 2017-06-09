package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.mvp.view.RecipeCollectionsView;
import com.alsash.reciper.ui.fragment.RecipeTabCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeTabGridFragment;
import com.alsash.reciper.ui.fragment.RecipeTabLabelFragment;

/**
 * A RecipeCollectionsPresenter
 */
public class RecipeCollectionsPresenter implements BasePresenter<RecipeCollectionsView> {

    private final Fragment[] collections = new Fragment[]{
            RecipeTabCategoryFragment.newInstance(),
            RecipeTabGridFragment.newInstance(),
            RecipeTabLabelFragment.newInstance()
    };

    @Override
    public void attach(RecipeCollectionsView view) {
        view.setCollections(collections);
        view.showCollection(1);
    }

    @Override
    public void visible(RecipeCollectionsView view) {
        // Do nothing
    }

    @Override
    public void invisible(RecipeCollectionsView view) {
        // Do nothing
    }

    @Override
    public void refresh(RecipeCollectionsView view) {
        // Do nothing
    }

    @Override
    public void detach() {
        // Do nothing
    }
}
