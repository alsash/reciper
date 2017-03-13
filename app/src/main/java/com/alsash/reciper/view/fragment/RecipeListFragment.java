package com.alsash.reciper.view.fragment;

import android.content.Context;
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
import com.alsash.reciper.view.adapter.RecipeCardAdapter;
import com.alsash.reciper.view.animator.FlipCardAnimator;

import java.util.List;


public class RecipeListFragment extends Fragment {

    // Model
    private List<Recipe> recipes;

    // Views
    private RecyclerView list;

    // Interactions
    // Decorator Pattern
    private RecipeListInteraction activityInteraction;
    private RecipeListInteraction decoratorInteraction = new RecipeListInteraction() {
        @Override
        public void onOpen(Recipe recipe, int position) {
            if (activityInteraction == null) return;
            activityInteraction.onOpen(recipe, position);
            updateList(recipe, position);
        }

        @Override
        public void onExpand(Recipe recipe, int position) {
            if (activityInteraction == null) return;
            activityInteraction.onExpand(recipe, position);
            list.getAdapter().notifyItemChanged(position);
            updateList(recipe, position);
        }
    };

    public static RecipeListFragment newInstance() {
        Bundle args = new Bundle();
        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeListInteraction) {
            activityInteraction = (RecipeListInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityInteraction = null;
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
        list.setAdapter(new RecipeCardAdapter(decoratorInteraction, recipes));
        list.setItemAnimator(new FlipCardAnimator());
    }

    private void updateList(Recipe recipe, int position) {
        if (recipes.get(position) != null && recipes.get(position).equals(recipe)) {
            list.getAdapter().notifyItemChanged(position);
        } else {
            list.getAdapter().notifyItemRemoved(position);
        }
    }
}
