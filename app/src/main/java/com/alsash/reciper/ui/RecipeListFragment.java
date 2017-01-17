package com.alsash.reciper.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alsash.reciper.R;

public class RecipeListFragment extends Fragment {

    public static RecipeListFragment newInstance() {

        Bundle args = new Bundle();

        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipe_list, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            // Group options
            case R.id.group_all:
            case R.id.group_bookmark:
            case R.id.group_category:
            case R.id.group_energy:
            case R.id.group_food:
            case R.id.group_label:
            case R.id.group_nutrition:
            case R.id.group_time:
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
            case R.id.filter_bookmark:
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

    private void resetRecipeListOptions() {

    }

    private void clearSearchPrevious() {

    }

    private void groupRecipeList(int groupMenuId) {
        switch (groupMenuId) {
            case R.id.group_all:
            case R.id.group_bookmark:
            case R.id.group_category:
            case R.id.group_energy:
            case R.id.group_food:
            case R.id.group_label:
            case R.id.group_nutrition:
            case R.id.group_time:
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
}
