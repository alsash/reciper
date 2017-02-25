package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.RecipeManager;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.contract.KeyContract;

public class RecipeDetailMainFragment extends Fragment {

    private Recipe recipe;


    public static RecipeDetailMainFragment newInstance(long recipeId) {
        Bundle args = new Bundle();
        args.putLong(KeyContract.KEY_RECIPE_ID, recipeId);
        RecipeDetailMainFragment fragment = new RecipeDetailMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long recipeId = getArguments().getLong(KeyContract.KEY_RECIPE_ID, -1);
        recipe = RecipeManager.getInstance().getRecipe(recipeId);
        assert recipe != null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_detail_main, container, false);
        setupChart(layout);
        return layout;
    }

    private void setupChart(View layout) {
//        pieChart = (PieChart) layout.findViewById(R.id.recipe_detail_main_pie_chart);


    }
}
