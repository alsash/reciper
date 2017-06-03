package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeTabGridPresenter;
import com.alsash.reciper.mvp.view.RecipeTabGridView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

import java.util.List;

import javax.inject.Inject;

/**
 * Simple final fragment with presenter and interactions
 */
public class RecipeTabGridFragment extends BaseRecipeListFragment<Recipe, RecipeTabGridView>
        implements RecipeTabGridView {

    @Inject
    RecipeTabGridPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeTabGridFragment newInstance() {
        return new RecipeTabGridFragment();
    }

    @Override
    protected RecipeTabGridPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiRecipeTabComponent()
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

    @Override
    protected void setupList() {
        super.setupList();
        list.setItemAnimator(new FlipCardListAnimator());
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context,
                context.getResources().getInteger(R.integer.recipe_list_span));
    }
}
