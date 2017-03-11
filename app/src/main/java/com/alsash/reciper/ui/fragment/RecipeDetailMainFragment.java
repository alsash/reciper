package com.alsash.reciper.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Typeface;
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
import com.alsash.reciper.ui.view.ArcProgressStackView;

import java.util.ArrayList;
import java.util.List;

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
        int[] colors = getResources().getIntArray(R.array.nutrition_colors);
        int[] backgrounds = getResources().getIntArray(R.array.nutrition_backgrounds);
        int[] values = new int[]{
                recipe.getNutrition().getCarbohydrate(),
                recipe.getNutrition().getProtein(),
                recipe.getNutrition().getFat(),
        };
        int length = backgrounds.length;
        if (length > colors.length) length = colors.length;
        if (length > values.length) length = values.length;

        List<ArcProgressStackView.Model> models = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            models.add(new ArcProgressStackView.Model(
                    getResources().getQuantityString(R.plurals.quantity_weight_gram, values[i], values[i]),
                    values[i],
                    backgrounds[i],
                    colors[i])
                    .setProgressConverter(null));
        }
        nutritionChart.setModels(models);
        nutritionChart.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        nutritionChart.setAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                int energy = (int) (fraction * recipe.getNutrition().getEnergy());
                nutritionEnergy.setText(getResources().getString(R.string.nutrition_energy_value, energy));
            }
        });
        nutritionChart.setAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                nutritionEnergy.setText(getResources().getString(
                        R.string.nutrition_energy_value,
                        recipe.getNutrition().getEnergy()));
            }
        });
        nutritionChart.animateProgress();
    }
}
