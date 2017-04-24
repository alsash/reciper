package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseRecipesView;
import com.alsash.reciper.ui.activity.RecipeDetailActivity;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

public abstract class BaseRecipesFragment extends Fragment implements BaseRecipesView {

    private RecyclerView recyclerView;

    protected abstract void setupList(RecyclerView list);

    @Override
    public void showDetails(Recipe recipe) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe.getId());
        bottomDialog.show(getActivity().getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void showRecipe(Recipe recipe) {
        RecipeDetailActivity.start(getContext(), recipe.getId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_list, container, false);
        bindViews(layout);
        setupList(recyclerView);
        return layout;
    }

    private void bindViews(View layout) {
        recyclerView = (RecyclerView) layout.findViewById(R.id.list);
    }
}
