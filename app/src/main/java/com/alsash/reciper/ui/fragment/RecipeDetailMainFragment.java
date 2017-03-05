package com.alsash.reciper.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.RecipeManager;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.contract.KeyContract;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ArcProgressStackView;

public class RecipeDetailMainFragment extends Fragment {

    // Models
    private Recipe recipe;

    // Views
    private ArcProgressStackView nutritionChart;
    private TextView nutritionEnergy;


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
        bindModels();
    }

    private void bindModels() {
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
        bindViews(layout);

        setupChart();
        return layout;
    }

    private void bindViews(View layout) {
        nutritionChart = (ArcProgressStackView) layout.findViewById(R.id.nutrition_chart);
        nutritionEnergy = (TextView) layout.findViewById(R.id.nutrition_energy);
    }

    private void setupChart() {
        String[] titles = getResources().getStringArray(R.array.nutrition_titles);

        int[] progress = new int[]{
                recipe.getNutrition().getCarbohydrate(),
                recipe.getNutrition().getProtein(),
                recipe.getNutrition().getFat()
        };

        String[] stringColors = getResources().getStringArray(R.array.nutrition_colors);
        int[] colors = new int[]{
                Color.parseColor(stringColors[0]),
                Color.parseColor(stringColors[1]),
                Color.parseColor(stringColors[2])
        };

        final List<ArcProgressStackView.Model> models = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            models.add(new ArcProgressStackView.Model(titles[i], progress[i], colors));
        }

        nutritionChart.setModels(models);
        nutritionEnergy.setText(getResources().getString(R.string.nutrition_energy_unit,
                recipe.getNutrition().getEnergy()));

    }
}
