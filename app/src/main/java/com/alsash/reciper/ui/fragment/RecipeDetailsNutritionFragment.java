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
import com.alsash.reciper.mvp.presenter.RecipeDetailsNutritionPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsNutritionView;
import com.alsash.reciper.ui.view.ArcProgressStackView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent recipe descriptions
 */
public class RecipeDetailsNutritionFragment extends BaseFragment<RecipeDetailsNutritionView>
        implements RecipeDetailsNutritionView {

    @Inject
    RecipeDetailsNutritionPresenter presenter;
    @Inject
    NavigationLogic navigator;

    // Nutrition card
    private SwitchCompat nutritionSwitch;
    private CompoundButton.OnCheckedChangeListener nutritionSwitchListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    presenter.onNutritionSwitch(isChecked, getThisView());
                }
            };
    private ArcProgressStackView nutritionChart;
    private TextView nutritionEnergy;
    private TextView nutritionCarbsPercent;
    private TextView nutritionProteinPercent;
    private TextView nutritionFatPercent;

    public static RecipeDetailsNutritionFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsNutritionFragment(), intent);
    }

    @Override
    public void showNutritionQuantity(int quantity, RecipeUnit recipeUnit) {
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
    public void showNutritionChart(final Nutrient nutrient) {
        // Energy
        setupEnergy(nutrient.getEnergy(), nutrient.getEnergyUnit());
        // Nutrition in percent
        setupWeightPercent(
                nutrient.getCarbsPercent(),
                nutrient.getProteinPercent(),
                nutrient.getFatPercent()
        );
        // Nutrition chart title
        String[] title;
        switch (nutrient.getWeightUnit()) {
            case GRAM:
                title = new String[]{
                        getString(R.string.quantity_weight_exact_gram, nutrient.getCarbs()),
                        getString(R.string.quantity_weight_exact_gram, nutrient.getProtein()),
                        getString(R.string.quantity_weight_exact_gram, nutrient.getFat())
                };
                break;
            case KILOGRAM:
                title = new String[]{
                        getString(R.string.quantity_weight_exact_kilogram, nutrient.getCarbs()),
                        getString(R.string.quantity_weight_exact_kilogram, nutrient.getProtein()),
                        getString(R.string.quantity_weight_exact_kilogram, nutrient.getFat())
                };
                break;
            default:
                return;
        }
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
        nutritionChart.setModels(models);
        nutritionChart.setAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                setupWeightPercent(
                        Math.round(fraction * nutrient.getCarbsPercent()),
                        Math.round(fraction * nutrient.getProteinPercent()),
                        Math.round(fraction * nutrient.getFatPercent())
                );
                setupEnergy(
                        Math.round(fraction * nutrient.getEnergy()),
                        nutrient.getEnergyUnit()
                );
            }
        });
    }

    @Override
    public void showNutritionAnimation() {
        nutritionChart.startAnimateProgress();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup group,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_nutrition, group, false);
        nutritionSwitch = (SwitchCompat) layout.findViewById(R.id.recipe_nutrition_switch);
        nutritionSwitch.setOnCheckedChangeListener(nutritionSwitchListener);
        nutritionChart = (ArcProgressStackView) layout.findViewById(R.id.nutrition_chart);
        nutritionEnergy = (TextView) layout.findViewById(R.id.nutrition_energy);
        nutritionCarbsPercent = (TextView) layout.findViewById(R.id.nutrition_carbohydrate_value);
        nutritionProteinPercent = (TextView) layout.findViewById(R.id.nutrition_protein_value);
        nutritionFatPercent = (TextView) layout.findViewById(R.id.nutrition_fat_value);
        return layout;
    }

    @Override
    protected RecipeDetailsNutritionPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }


    private void setupEnergy(int quantity, EnergyUnit unit) {
        switch (unit) {
            case CALORIE:
                nutritionEnergy.setText(getString(R.string.quantity_energy_calorie_n, quantity));
                break;
            case KILOCALORIE:
                nutritionEnergy.setText(getString(R.string.quantity_energy_kilocalorie_n,
                        quantity));
                break;
        }
    }

    private void setupWeightPercent(int carbsPercent, int proteinPercent, int fatPercent) {
        nutritionCarbsPercent.setText(getString(R.string.quantity_percent, carbsPercent));
        nutritionProteinPercent.setText(getString(R.string.quantity_percent, proteinPercent));
        nutritionFatPercent.setText(getString(R.string.quantity_percent, fatPercent));
    }
}
