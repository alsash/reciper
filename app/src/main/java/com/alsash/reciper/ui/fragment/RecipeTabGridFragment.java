package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeTabGridPresenter;
import com.alsash.reciper.mvp.view.RecipeTabGridView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.util.List;

import javax.inject.Inject;

/**
 * Simple final fragment with presenter and interactions
 */
public class RecipeTabGridFragment extends BaseListFragment<Recipe, RecipeTabGridView>
        implements RecipeTabGridView, RecipeListInteraction {

    @Inject
    RecipeTabGridPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeTabCategoryFragment newInstance() {
        return new RecipeTabCategoryFragment();
    }

    @Override
    protected RecipeTabGridPresenter inject() {
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
