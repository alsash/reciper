package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeCategoryPresenter;
import com.alsash.reciper.mvp.view.RecipeCategoryView;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.util.List;

import javax.inject.Inject;

public class RecipeCategoryFragment extends BaseFragment implements RecipeCategoryView {

    @Inject
    private RecipeCategoryPresenter presenter;

    private SwipeRefreshLayout refreshIndicator;
    private RecyclerView list;
    private RecipeGroupCardListAdapter adapter;

    public static RecipeCategoryFragment newInstance() {
        return new RecipeCategoryFragment();
    }

    @Override
    protected BasePresenter setupPresenter() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getRecipeListComponent()
                .inject(this);
        presenter.setView(this);
        return presenter; // Presenter will be embedded in activity lifecycle
    }

    @Override
    public void setCategories(List<Category> categories) {
        adapter = new RecipeGroupCardListAdapter(presenter, categories);
    }

    @Override
    public void showCategories() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showRecipeDetails(Recipe recipe) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe.getId());
        bottomDialog.show(getActivity().getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void showLoading(Boolean isLoading) {
        refreshIndicator.setRefreshing(isLoading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_list_refresh, container, false);
        bindViews(layout);
        setupList();
        setupRefresh();
        return layout;
    }

    private void bindViews(View layout) {
        refreshIndicator = (SwipeRefreshLayout) layout.findViewById(R.id.refresh_indicator);
        list = (RecyclerView) layout.findViewById(R.id.refresh_list);
    }

    private void setupList() {
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        list.setAdapter(adapter); // Created at setCategories()
    }

    private void setupRefresh() {
        refreshIndicator.setColorSchemeResources(
                R.color.nutrition_carbohydrate,
                R.color.nutrition_fat,
                R.color.nutrition_protein);
    }
}
