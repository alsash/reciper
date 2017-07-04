package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeDetailsDescriptionPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;
import com.alsash.reciper.ui.adapter.RecipeLabelListAdapter;
import com.alsash.reciper.ui.adapter.interaction.LabelInteraction;
import com.alsash.reciper.ui.loader.ImageLoader;

import java.text.SimpleDateFormat;
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
            presenter.addLabel();
        }
    };
    private RecyclerView labelsList;
    private RecipeLabelListAdapter labelsListAdapter;
    private LabelInteraction labelsListInteraction = new LabelInteraction() {
        @Override
        public void onPress(Label label) {
            presenter.deleteLabel(label);
        }
    };
    // Time card
    private TextView recipeTime;
    private ImageButton recipeTimeEdit;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDateFormat = new SimpleDateFormat(getString(R.string.date_format_recipe),
                Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup group,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_description, group, false);
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
