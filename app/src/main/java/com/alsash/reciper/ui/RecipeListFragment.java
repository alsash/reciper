package com.alsash.reciper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.adapter.RecipeListAdapter;
import com.alsash.reciper.ui.animator.RecipeListAnimator;
import com.alsash.reciper.ui.vector.VectorHelper;

public class RecipeListFragment extends Fragment {

    RecipeListAdapter.OnRecipeInteraction recipeInteraction;

    public static RecipeListFragment newInstance() {

        Bundle args = new Bundle();

        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        recipeInteraction = (RecipeListAdapter.OnRecipeInteraction) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recipeInteraction = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
        recyclerView.setAdapter(new RecipeListAdapter(recipeInteraction));
        recyclerView.setItemAnimator(new RecipeListAnimator());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getActivity().getMenuInflater().inflate(R.menu.recipe_list, menu);
        new VectorHelper(getActivity()).tintMenuItems(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.search:
                // Call to Search Activity;
                return true;

            // Group options
            case R.id.group_all:
            case R.id.group_category:
            case R.id.group_food:
            case R.id.group_label:
                groupRecipeList(id);
                return true;

            // Sort options
            case R.id.sort_account:
            case R.id.sort_clear:
            case R.id.sort_date_top:
            case R.id.sort_date_end:
            case R.id.sort_name:
            case R.id.sort_rating:
                sortRecipeList(id);
                return true;

            // Filter options
            case R.id.filter_account:
            case R.id.filter_category:
            case R.id.filter_clear:
            case R.id.filter_date_top:
            case R.id.filter_date_end:
            case R.id.filter_energy:
            case R.id.filter_food:
            case R.id.filter_label:
            case R.id.filter_name:
            case R.id.filter_nutrition:
            case R.id.filter_rating:
            case R.id.filter_time:
                filterRecipeList(id);

            case R.id.search_clear:
                clearSearchPrevious();
                return true;

            case R.id.reset:
                resetRecipeListOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void groupRecipeList(int groupMenuId) {
        switch (groupMenuId) {
            case R.id.group_all:
            case R.id.group_category:
            case R.id.group_food:
            case R.id.group_label:
        }
    }

    private void sortRecipeList(int sortMenuId) {

        switch (sortMenuId) {
            case R.id.sort_account:
            case R.id.sort_clear:
            case R.id.sort_date_top:
            case R.id.sort_date_end:
            case R.id.sort_name:
            case R.id.sort_rating:
        }
    }

    private void filterRecipeList(int filterMenuId) {

    }

    private void resetRecipeListOptions() {

    }

    private void clearSearchPrevious() {

    }
}
