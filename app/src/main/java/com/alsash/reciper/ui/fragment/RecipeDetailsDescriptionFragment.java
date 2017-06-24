package com.alsash.reciper.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.logic.unit.EnergyUnit;
import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.derivative.Nutrient;
import com.alsash.reciper.mvp.presenter.RecipeDetailsDescriptionPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;
import com.alsash.reciper.ui.view.ArcProgressStackView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent recipe descriptions
 */
public class RecipeDetailsDescriptionFragment extends BaseFragment<RecipeDetailsDescriptionView>
        implements RecipeDetailsDescriptionView {

    private static final String TAG = "RecipeDetailsDescriptionFragment";
    private static final String KEY_STATE_CHART_ANIMATED = TAG + ".key_state_chart_animated";

    @Inject
    RecipeDetailsDescriptionPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private SwitchCompat nutritionSwitch;
    private ArcProgressStackView nutritionChart;
    private TextView nutritionEnergy;
    private TextView nutritionCarbsPercent;
    private TextView nutritionProteinPercent;
    private TextView nutritionFatPercent;

    public static RecipeDetailsDescriptionFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsDescriptionFragment(), intent);
    }

    @Override
    public void showNutritionSwitch(int quantity, RecipeUnit recipeUnit) {
        switch (recipeUnit) {
            case GRAM:
                nutritionSwitch.setText(
                        getString(R.string.nutrition_quantity_switch,
                                getResources().getQuantityString(
                                        R.plurals.quantity_weight_gram, quantity, quantity)));
                break;
            case SERVING:
                nutritionSwitch.setText(
                        getString(R.string.nutrition_quantity_switch,
                                getResources().getQuantityString(
                                        R.plurals.quantity_serving, quantity, quantity)));
                break;
        }
    }

    @Override
    public void showNutritionValue(final Nutrient nutrient, boolean animated) {
        // Energy
        setupEnergy(nutrient.getEnergyUnit(), nutrient.getEnergy());
        // Nutrition in percent
        nutritionCarbsPercent.setText(getString(R.string.percent, nutrient.getCarbsPercent()));
        nutritionProteinPercent.setText(getString(R.string.percent, nutrient.getProteinPercent()));
        nutritionFatPercent.setText(getString(R.string.percent, nutrient.getFatPercent()));

        // Nutrition chart title
        int weightPlurals;
        switch (nutrient.getWeightUnit()) {
            case GRAM:
                weightPlurals = R.plurals.quantity_weight_gram;
                break;
            case KILOGRAM:
                weightPlurals = R.plurals.quantity_weight_kilogram;
                break;
            default:
                return;
        }
        String[] title = new String[]{
                getResources().getQuantityString(weightPlurals,
                        nutrient.getCarbs(), nutrient.getCarbs()),
                getResources().getQuantityString(weightPlurals,
                        nutrient.getProtein(), nutrient.getProtein()),
                getResources().getQuantityString(weightPlurals,
                        nutrient.getFat(), nutrient.getFat()),
        };
        // Nutrition chart progress
        final int[] progress = new int[]{
                nutrient.getCarbsPercent(),
                nutrient.getProteinPercent(),
                nutrient.getFatPercent(),
        };
        // Nutrition chart colors
        int[] colors = getResources().getIntArray(R.array.nutrition_colors);
        int[] backgrounds = getResources().getIntArray(R.array.nutrition_backgrounds);
        // Nutrition chart models
        List<ArcProgressStackView.Model> models = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            models.add(new ArcProgressStackView.Model(
                    title[i],
                    progress[i],
                    backgrounds[i],
                    colors[i]));
        }
        nutritionChart.setIsAnimated(true);
        nutritionChart.setModels(models);
        nutritionChart.setAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                nutritionCarbsPercent.setText(getString(R.string.percent,
                        Math.round(fraction * nutrient.getCarbsPercent())));
                nutritionProteinPercent.setText(getString(R.string.percent,
                        Math.round(fraction * nutrient.getProteinPercent())));
                nutritionFatPercent.setText(getString(R.string.percent,
                        Math.round(fraction * nutrient.getFatPercent())));
                setupEnergy(nutrient.getEnergyUnit(),
                        Math.round(fraction * nutrient.getEnergy()));
            }
        });
        if (animated) nutritionChart.startAnimateProgress();
    }

    private void setupEnergy(EnergyUnit unit, int quantity) {
        switch (unit) {
            case CALORIE:
                nutritionEnergy.setText(
                        getResources().getQuantityString(R.plurals.quantity_energy_calorie,
                                quantity, quantity));
                break;
            case KILOCALORIE:
                nutritionEnergy.setText(
                        getResources().getQuantityString(R.plurals.quantity_energy_kilocalorie,
                                quantity, quantity));
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_description,
                container, false);
        nutritionSwitch = (SwitchCompat) layout.findViewById(R.id.recipe_nutrition_switch);
        nutritionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.onNutritionSwitch(isChecked, getThisView());
            }
        });
        nutritionChart = (ArcProgressStackView) layout.findViewById(R.id.nutrition_chart);
        nutritionEnergy = (TextView) layout.findViewById(R.id.nutrition_energy);
        nutritionCarbsPercent = (TextView) layout.findViewById(R.id.nutrition_carbohydrate_value);
        nutritionProteinPercent = (TextView) layout.findViewById(R.id.nutrition_protein_value);
        nutritionFatPercent = (TextView) layout.findViewById(R.id.nutrition_fat_value);
        return layout;
    }

    @Override
    protected RecipeDetailsDescriptionPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }


}
