package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeRestrictListPresenter;
import com.alsash.reciper.mvp.view.RecipeRestrictListView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent list of recipe cards
 */
public class RecipeRestrictListFragment extends BaseListFragment<Recipe, RecipeRestrictListView>
        implements RecipeRestrictListView, RecipeListInteraction {

    @Inject
    RecipeRestrictListPresenter presenter;
    @Inject
    NavigationLogic navigator;


    public static RecipeRestrictListFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeRestrictListFragment(), intent);
    }

    @Override
    public void onFavorite(Recipe recipe) {
        presenter.changeFavorite(recipe);
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe);
    }

    @Override
    public void setTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }

    @Override
    protected RecipeRestrictListPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeRestrictListComponent()
                .inject(this);
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Recipe> container) {
        return new RecipeCardListAdapter(this, container, R.layout.item_recipe_card);
    }

    @Override
    protected void setupList() {
        super.setupList();
        list.setItemAnimator(new FlipCardListAnimator());
    }

    @Override
    public void showLoading(boolean loading) {
        if (refreshIndicator != null)
            refreshIndicator.setRefreshing(loading);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context,
                context.getResources().getInteger(R.integer.recipe_list_span));
    }
}
