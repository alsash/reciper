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
import com.alsash.reciper.mvp.presenter.BaseWeakPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabCategoryWeakPresenter;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.util.List;

import javax.inject.Inject;

public class RecipeTabTabCategoryFragment extends BaseFragment implements RecipeTabCategoryView {

    @Inject
    RecipeTabCategoryWeakPresenter presenter;

    private SwipeRefreshLayout refreshIndicator;
    private RecyclerView list;
    private RecipeGroupCardListAdapter adapter;

    public static RecipeTabTabCategoryFragment newInstance() {
        return new RecipeTabTabCategoryFragment();
    }

    @Override
    protected BaseWeakPresenter setupPresenter() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getRecipeTabComponent()
                .inject(this);
        presenter.setView(this);
        return presenter; // BasePresenter will be embedded in fragment lifecycle
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(list.getContext());
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter); // Created at setCategories()
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            });

    }

    private void setupRefresh() {
        refreshIndicator.setColorSchemeResources(
                R.color.nutrition_carbohydrate,
                R.color.nutrition_fat,
                R.color.nutrition_protein);
    }
}
