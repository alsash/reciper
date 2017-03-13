package com.alsash.reciper.view.fragment;

import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.model.RecipeManager;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.view.contract.KeyContract;
import com.alsash.reciper.view.views.ArcProgressStackView;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailMainFragment extends Fragment {

    private static final String KEY_STATE_IS_CHART_ANIMATED = "STATE_IS_CHART_ANIMATED";

    // State
    private boolean isChartAnimated;

    // Model
    private Recipe recipe;

    // Views
    private SwitchCompat nutritionSwitch;
    private ArcProgressStackView nutritionChart;
    private TextView nutritionEnergy;
    private TextView nutritionCarbohydrate;
    private TextView nutritionProtein;
    private TextView nutritionFat;

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
        restoreInstanceState(savedInstanceState);
        bindModel();
    }

    private void bindModel() {
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
        setupSwitch();
        setupChart();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isChartAnimated) {
            isChartAnimated = true;
            nutritionChart.startAnimateProgress();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isChartAnimated) {
            nutritionChart.endAnimateProgress();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STATE_IS_CHART_ANIMATED, isChartAnimated);
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) return;
        isChartAnimated = savedInstanceState.getBoolean(KEY_STATE_IS_CHART_ANIMATED);
    }

    private void bindViews(View layout) {
        nutritionSwitch = (SwitchCompat) layout.findViewById(R.id.card_nutrition_quantity_switch);
        nutritionChart = (ArcProgressStackView) layout.findViewById(R.id.nutrition_chart);
        nutritionEnergy = (TextView) layout.findViewById(R.id.nutrition_energy);
        nutritionCarbohydrate = (TextView) layout.findViewById(R.id.nutrition_carbohydrate_value);
        nutritionProtein = (TextView) layout.findViewById(R.id.nutrition_protein_value);
        nutritionFat = (TextView) layout.findViewById(R.id.nutrition_fat_value);
    }

    private void setupSwitch() {
        setupSwitchText();
        nutritionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupSwitchText();
                nutritionChart.startAnimateProgress();
            }
        });
    }

    private void setupSwitchText() {
        if (nutritionSwitch.isChecked()) {
            nutritionSwitch.setText(getString(R.string.nutrition_quantity_switch,
                    getResources().getQuantityString(R.plurals.quantity_serving, 1, 1)));
        } else {
            nutritionSwitch.setText(getString(R.string.nutrition_quantity_switch,
                    getResources().getQuantityString(R.plurals.quantity_weight_gram, 100, 100)));
        }
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
                int carbohydrate = (int) (fraction * recipe.getNutrition().getCarbohydrate());
                int protein = (int) (fraction * recipe.getNutrition().getProtein());
                int fat = (int) (fraction * recipe.getNutrition().getFat());
                nutritionEnergy.setText(getResources().getString(R.string.nutrition_energy_value, energy));
                nutritionCarbohydrate.setText(getString(R.string.percent, carbohydrate));
                nutritionProtein.setText(getString(R.string.percent, protein));
                nutritionFat.setText(getString(R.string.percent, fat));

            }
        });
    }
}
