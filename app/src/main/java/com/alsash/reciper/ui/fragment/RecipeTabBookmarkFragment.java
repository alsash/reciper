package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeTabBookmarkPresenter;
import com.alsash.reciper.mvp.view.RecipeTabBookmarkView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * Simple final fragment with presenter and interactions
 */
public class RecipeTabBookmarkFragment extends BaseRecipeListFragment<Recipe, RecipeTabBookmarkView>
        implements RecipeTabBookmarkView {

    @Inject
    RecipeTabBookmarkPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeTabBookmarkFragment newInstance() {
        return new RecipeTabBookmarkFragment();
    }

    @Override
    protected RecipeTabBookmarkPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getRecipeTabComponent()
                .inject(this);
        return presenter; // Presenter will be embedded in fragment lifecycle
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Recipe> container) {
        return new RecipeCardListAdapter(this, container);
    }

    @Override
    protected AppNavigator getNavigator() {
        return navigator;
    }
}
