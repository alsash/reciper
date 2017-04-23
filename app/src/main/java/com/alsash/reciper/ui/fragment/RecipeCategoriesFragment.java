package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alsash.reciper.database.ApiDatabase;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.presenter.RecipeCategoriesPresenter;
import com.alsash.reciper.mvp.view.RecipeCategoriesView;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategoriesFragment
        extends BaseRecipesFragment<RecipeCategoriesView, RecipeCategoriesPresenter>
        implements RecipeCategoriesView {

    private RecipeCategoriesPresenter presenter = new RecipeCategoriesPresenter(
            ApiDatabase.getInstance().getSession(getContext()));

    private List<Category> categories = new ArrayList<>();

    private RecyclerView list;

    public static RecipeCategoriesFragment newInstance() {
        return new RecipeCategoriesFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadCategories();
    }

    @Override
    public void addCategories(List<Category> newCategories) {
        categories.addAll(newCategories);
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showContent() {
        list.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void setupList(RecyclerView parentList) {
        this.list = parentList;
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(new RecipeGroupCardListAdapter(presenter, categories));
        list.setNestedScrollingEnabled(false);
    }

    @Override
    public RecipeCategoriesPresenter createPresenter() {
        return presenter;
    }
}
