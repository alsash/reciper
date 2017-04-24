package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.database.ApiDatabase;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.presenter.RecipeCategoriesPresenter;
import com.alsash.reciper.mvp.view.RecipeCategoriesView;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategoriesFragment extends BaseRecipesFragment implements RecipeCategoriesView {

    private RecipeCategoriesPresenter presenter;

    private List<Category> categories = new ArrayList<>();
    private RecyclerView listView;

    public static RecipeCategoriesFragment newInstance() {
        return new RecipeCategoriesFragment();
    }

    // Super implementations
    @Override
    protected void setupList(RecyclerView parentList) {
        listView = parentList;
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setAdapter(new RecipeGroupCardListAdapter(presenter, categories));
        listView.setNestedScrollingEnabled(false);
    }

    // Interfaces implementations
    @Override
    public void addCategories(List<Category> newCategories) {
        categories.addAll(newCategories);
        listView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showCategories() {
        listView.getAdapter().notifyDataSetChanged();
    }

    // This implementations
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipeCategoriesPresenter(
                ApiDatabase.getInstance().getSession(getContext())
        );
        presenter.setView(this); // Init view
        presenter.loadCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setView(this); // Attach view
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.setView(null); // Detach view
    }
}
