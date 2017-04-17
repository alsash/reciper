package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.alsash.reciper.mvp.model.entity.RecipeGroup;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;

import java.util.List;

public class RecipeGroupListFragment extends BaseRecipeListFragment {

    // Model
    private List<RecipeGroup> groups;

    @Override
    protected void bindModel() {

    }

    @Override
    protected void setupList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecipeGroupCardListAdapter(this, groups));
        recyclerView.setNestedScrollingEnabled(false);
    }
}
