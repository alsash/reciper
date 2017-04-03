package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.Recipe;
import com.alsash.reciper.ui.adapter.RecipeSingleCardListAdapter;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

import java.util.List;


public class RecipeSingleListFragment extends BaseRecipeListFragment {

    // Model
    private List<Recipe> recipes;

    public static RecipeSingleListFragment newInstance() {
        Bundle args = new Bundle();
        RecipeSingleListFragment fragment = new RecipeSingleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindModel() {
    }

    @Override
    protected void setupList() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources()
                .getInteger(R.integer.recipe_list_span)));
        recyclerView.setAdapter(new RecipeSingleCardListAdapter(this, recipes));
        recyclerView.setItemAnimator(new FlipCardListAnimator());
    }
}
