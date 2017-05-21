package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeTabBookmarkPresenter;
import com.alsash.reciper.mvp.view.RecipeTabBookmarkView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.util.List;

import javax.inject.Inject;

/**
 * Simple final fragment with presenter and interactions
 */
public class RecipeTabBookmarkFragment extends BaseListFragment<Recipe, RecipeTabBookmarkView>
        implements RecipeTabBookmarkView, RecipeListInteraction {

    @Inject
    RecipeTabBookmarkPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeTabCategoryFragment newInstance() {
        return new RecipeTabCategoryFragment();
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
    public void onExpand(Recipe recipe) {
        RecipeBottomDialog.show(recipe.getId(), getActivity().getSupportFragmentManager());
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.toRecipeView(recipe.getId());
    }
}
