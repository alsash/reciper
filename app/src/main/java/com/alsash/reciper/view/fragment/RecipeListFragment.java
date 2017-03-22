package com.alsash.reciper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.model.RecipeManager;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.activity.RecipeDetailActivity;
import com.alsash.reciper.view.adapter.RecipeCardAdapter;
import com.alsash.reciper.view.animator.FlipCardAnimator;
import com.alsash.reciper.view.fragment.dialog.RecipeBottomDialog;

import java.util.List;


public class RecipeListFragment extends Fragment implements RecipeListInteraction {

    // Model
    private List<Recipe> recipes;

    // Views
    private RecyclerView list;

    public static RecipeListFragment newInstance() {
        Bundle args = new Bundle();
        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onExpand(Recipe recipe, int position) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe);
        bottomDialog.show(getActivity().getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void onOpen(Recipe recipe, int position) {
        RecipeDetailActivity.start(getContext(), recipe.getId());
        list.getAdapter().notifyItemChanged(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_list, container, false);
        bindViews(layout);
        setupList();
        return layout;
    }

    private void bindModel() {
        recipes = RecipeManager.getInstance().getRecipes();
    }

    private void bindViews(View layout) {
        list = (RecyclerView) layout.findViewById(R.id.list);
    }

    private void setupList() {
        list.setLayoutManager(new GridLayoutManager(getActivity(), getResources()
                .getInteger(R.integer.recipe_list_span)));
        list.setAdapter(new RecipeCardAdapter(this, recipes));
        list.setItemAnimator(new FlipCardAnimator());
    }
}
