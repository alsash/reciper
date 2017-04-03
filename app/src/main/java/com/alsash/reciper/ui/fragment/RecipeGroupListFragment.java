package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import com.alsash.reciper.api.CategoryManager;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;
import com.alsash.reciper.ui.presenter.entity.Recipe;
import com.alsash.reciper.ui.presenter.interaction.RecipeListInteraction;

import java.util.List;

public class RecipeGroupListFragment extends Fragment implements RecipeListInteraction {

    // Model
    private List<BaseGroup<Recipe>> groups;

    public static RecipeGroupListFragment newInstance() {
        Bundle args = new Bundle();
        RecipeGroupListFragment fragment = new RecipeGroupListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void bindModel() {
        categories = CategoryManager.getInstance().list();
    }

    private void setupList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecipeGroupCardListAdapter(this, categories));
        recyclerView.setNestedScrollingEnabled(false);
    }
}
