package com.alsash.reciper.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.logic.unit.EnergyUnit;
import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.derivative.Nutrient;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeDetailsDescriptionPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;
import com.alsash.reciper.ui.adapter.RecipeLabelListAdapter;
import com.alsash.reciper.ui.adapter.interaction.LabelInteraction;
import com.alsash.reciper.ui.loader.ImageLoader;
import com.alsash.reciper.ui.view.ArcProgressStackView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A fragment that represent recipe descriptions
 */
public class RecipeDetailsDescriptionFragment extends BaseFragment<RecipeDetailsDescriptionView>
        implements RecipeDetailsDescriptionView {

    @Inject
    RecipeDetailsDescriptionPresenter presenter;
    @Inject
    NavigationLogic navigator;

    // Description card
    private ImageView authorImage;
    private TextView authorName;
    private TextView recipeSource;
    private TextView recipeDate;
    private TextView recipeDescription;
    // Category card
    private ImageView categoryImage;
    private TextView categoryName;
    private ImageButton categoryEdit;
    // Labels card
    private ImageButton labelsAdd;
    private TextView labelsTitle;
    private View.OnClickListener labelsAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onLabelAdd();
        }
    };
    private RecyclerView labelsList;
    private RecipeLabelListAdapter labelsListAdapter;
    private LabelInteraction labelsListInteraction = new LabelInteraction() {
        @Override
        public void onPress(Label label) {
            presenter.onLabelDelete(label);
        }
    };
    // Time card
    private TextView recipeTime;
    private ImageButton recipeTimeEdit;

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

    private SimpleDateFormat recipeDateFormat; // created at OnCreate()

    public static RecipeDetailsDescriptionFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsDescriptionFragment(), intent);
    }

    @Override
    public void showDescription(Recipe recipe) {
        ImageLoader.get().source(recipe.getAuthor()).load(authorImage);
        authorName.setText(recipe.getAuthor().getName());
        recipeSource.setText(recipe.getSource());
        recipeDate.setText(recipeDateFormat.format(recipe.getCreatedAt()));
        recipeDescription.setText(recipe.getDescription());
    }

    @Override
    public void showCategory(Category category) {
        ImageLoader.get().source(category.getPhoto()).load(categoryImage);
        categoryName.setText(category.getName());
    }

    @Override
    public void showLabels(List<Label> labels) {
        labelsTitle.setText(getResources().getQuantityString(R.plurals.quantity_label,
                labels.size(), labels.size()));
        if (labelsList == null) return;
        labelsListAdapter = new RecipeLabelListAdapter(labels, labelsListInteraction);
        labelsList.setAdapter(labelsListAdapter);
        labelsList.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) labelsList.getLayoutManager();
        lm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        // Calculating span count
        int maxSpans = getResources().getInteger(R.integer.recipe_labels_list_span);
        int spanCount = (int) Math.ceil((double) labels.size() / (double) maxSpans);
        if (spanCount <= 0) spanCount = 1;
        if (spanCount > maxSpans) spanCount = maxSpans;
        lm.setSpanCount(spanCount);
        labelsList.setLayoutManager(lm);
    }

    @Override
    public void showCookTime(Calendar calendar) {
        int h = calendar.get(Calendar.HOUR);
        int m = calendar.get(Calendar.MINUTE);
        String hours = getResources().getQuantityString(R.plurals.time_format_hours, h, h);
        String minutes = getResources().getQuantityString(R.plurals.time_format_minutes, m, m);
        recipeTime.setText((hours + " " + minutes).trim());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDateFormat = new SimpleDateFormat(getString(R.string.date_format_recipe),
                Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_description,
                container, false);
        // Description card
        authorImage = (ImageView) layout.findViewById(R.id.recipe_description_author_image);
        authorName = (TextView) layout.findViewById(R.id.recipe_description_author_name);
        recipeSource = (TextView) layout.findViewById(R.id.recipe_description_source);
        recipeDate = (TextView) layout.findViewById(R.id.recipe_description_date);
        recipeDescription = (TextView) layout.findViewById(R.id.recipe_description_body);
        // Category card
        categoryImage = (ImageView) layout.findViewById(R.id.recipe_category_image);
        categoryName = (TextView) layout.findViewById(R.id.recipe_category_name);
        categoryEdit = (ImageButton) layout.findViewById(R.id.recipe_category_edit);
        // Labels card
        labelsAdd = (ImageButton) layout.findViewById(R.id.recipe_labels_add);
        labelsAdd.setOnClickListener(labelsAddListener);
        labelsTitle = (TextView) layout.findViewById(R.id.recipe_labels_title);
        labelsList = (RecyclerView) layout.findViewById(R.id.recipe_labels_list);
        // Time card
        recipeTimeEdit = (ImageButton) layout.findViewById(R.id.recipe_time_edit);
        recipeTime = (TextView) layout.findViewById(R.id.recipe_time_value);
        // Nutrition card
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
    protected RecipeDetailsDescriptionPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }
}
