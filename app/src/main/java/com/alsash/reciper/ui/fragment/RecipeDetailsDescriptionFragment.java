package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableLong;
import com.alsash.reciper.app.lib.TimeLong;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeDetailsDescriptionPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;
import com.alsash.reciper.ui.adapter.RecipeLabelListAdapter;
import com.alsash.reciper.ui.fragment.dialog.SimpleDialog;
import com.alsash.reciper.ui.loader.ImageLoader;

import java.text.SimpleDateFormat;
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
    private View.OnClickListener descriptionEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.requestDescriptionEdit(getThisView());
        }
    };
    private View.OnClickListener authorEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.requestAuthorEdit(getThisView());
        }
    };
    private ImageButton descriptionEditButton;
    private ImageView authorImage;
    private TextView authorName;
    private EditText recipeSource;
    private TextView recipeDate;
    private EditText recipeDescription;

    // Category card
    private View.OnClickListener categoryEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.requestCategoryEdit(getThisView());
        }
    };
    private ImageView categoryImage;
    private TextView categoryName;
    private ImageButton categoryEditButton;
    // Labels card
    private View.OnClickListener labelsEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.requestLabelsEdit(getThisView());
        }
    };
    private ImageButton labelsEdit;
    private View labelsDivider;
    private TextView labelsTitle;
    private RecyclerView labelsList;
    // Time card
    private TextView recipeTime;
    private ImageButton recipeTimeEdit;
    private View.OnClickListener recipeTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.requestRecipeTime(getThisView());
        }
    };
    // Servings card
    private EditText servingsValue;
    private TextView servingsUnit;
    private ImageButton servingsEdit;
    private boolean servingsEditable = false;
    private View.OnClickListener servingsEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            servingsEditable = !servingsEditable;
            setEditable(servingsEditable, servingsEdit, servingsValue);
            if (!servingsEditable)
                presenter.requestServingsEdit(getThisView(), servingsValue.getText().toString());
        }
    };

    private SimpleDateFormat recipeDateFormat; // created at OnCreate()

    public static RecipeDetailsDescriptionFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsDescriptionFragment(), intent);
    }

    @Override
    public void showDescription(Recipe recipe) {
        recipeDate.setText(recipeDateFormat.format(recipe.getCreatedAt()));
        recipeDescription.setText(recipe.getDescription());
        recipeSource.setText(recipe.getSource());
    }

    @Override
    public String[] getDescriptionEditable() {
        return new String[]{
                recipeDescription.getText().toString(),
                recipeSource.getText().toString()};
    }

    @Override
    public void setDescriptionEditable(boolean editable) {
        setEditable(editable, descriptionEditButton, recipeDescription, recipeSource);
    }

    @Override
    public void showAuthor(Author author) {
        ImageLoader.get().source(author).load(authorImage);
        authorName.setText(author.getName());
    }

    @Override
    public void showCategory(Category category) {
        ImageLoader.get().source(category.getPhoto()).load(categoryImage);
        categoryName.setText(category.getName());
    }

    @Override
    public void showLabels(List<Label> labels) {
        if (labelsTitle != null)
            labelsTitle.setText(getResources().getQuantityString(R.plurals.quantity_label,
                    labels.size(), labels.size()));
        if (labelsDivider != null)

            if (labelsList == null) return;
        if (labels.size() == 0) {
            labelsList.setVisibility(View.GONE);
            if (labelsDivider != null) labelsDivider.setVisibility(View.GONE);
            return;
        }
        if (labelsDivider != null) labelsDivider.setVisibility(View.VISIBLE);
        labelsList.setVisibility(View.VISIBLE);
        labelsList.setAdapter(new RecipeLabelListAdapter(labels));
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
    public void showCookTime(long millis) {
        if (recipeTime == null) return;
        Resources r = getResources();
        int[] hm = TimeLong.getHoursAndMinutes(millis);
        String h = (hm[0] > 0) ? r.getQuantityString(R.plurals.time_format_hour, hm[0], hm[0]) : "";
        String m = r.getQuantityString(R.plurals.time_format_minute, hm[1], hm[1]);
        recipeTime.setText((h + " " + m).trim());
    }

    @Override
    public void showServings(int servings) {
        if (servingsValue != null)
            servingsValue.setText(String.valueOf(servings));
        if (servingsUnit != null)
            servingsUnit.setText(getResources()
                    .getQuantityString(R.plurals.quantity_serving_unit, servings));
    }

    @Override
    public void showCategoryEditDialog(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDialogCategoryView(recipe);
    }

    @Override
    public void showAuthorEditDialog(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDialogAuthorView(recipe);
    }

    @Override
    public void showLabelEditDialog(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDialogLabelView(recipe);
    }

    @Override
    public void showCookTimeEditDialog(MutableLong listener) {
        SimpleDialog.showEditTime(getActivity(), listener);
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
        descriptionEditButton = (ImageButton) layout.findViewById(R.id.recipe_description_edit);
        descriptionEditButton.setOnClickListener(descriptionEditListener);
        authorImage = (ImageView) layout.findViewById(R.id.recipe_description_author_image);
        authorImage.setOnClickListener(authorEditListener);
        authorName = (TextView) layout.findViewById(R.id.recipe_description_author_name);
        recipeSource = (EditText) layout.findViewById(R.id.recipe_description_source);
        recipeDate = (TextView) layout.findViewById(R.id.recipe_description_date);
        recipeDescription = (EditText) layout.findViewById(R.id.recipe_description_body);

        // Category card
        categoryImage = (ImageView) layout.findViewById(R.id.recipe_category_image);
        categoryName = (TextView) layout.findViewById(R.id.recipe_category_name);
        categoryEditButton = (ImageButton) layout.findViewById(R.id.recipe_category_edit);
        categoryEditButton.setOnClickListener(categoryEditListener);
        // Labels card
        labelsEdit = (ImageButton) layout.findViewById(R.id.recipe_labels_edit);
        labelsEdit.setOnClickListener(labelsEditListener);
        labelsTitle = (TextView) layout.findViewById(R.id.recipe_labels_title);
        labelsList = (RecyclerView) layout.findViewById(R.id.recipe_labels_list);
        labelsDivider = layout.findViewById(R.id.recipe_labels_horizontal_divider);
        // Time card
        recipeTimeEdit = (ImageButton) layout.findViewById(R.id.recipe_time_edit);
        recipeTimeEdit.setOnClickListener(recipeTimeListener);
        recipeTime = (TextView) layout.findViewById(R.id.recipe_time_value);

        // Servings card
        servingsValue = (EditText) layout.findViewById(R.id.recipe_serving_value);
        servingsUnit = (TextView) layout.findViewById(R.id.recipe_serving_unit);
        servingsEdit = (ImageButton) layout.findViewById(R.id.recipe_serving_edit);
        servingsEdit.setOnClickListener(servingsEditListener);
        setEditable(servingsEditable, servingsEdit, servingsValue);
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

    private void setEditable(boolean editable, ImageButton button, EditText... editTexts) {
        button.setImageResource(editable ?
                R.drawable.edit_icon_orange :
                R.drawable.edit_icon_gray);
        for (EditText editText : editTexts) {
            editText.setEnabled(editable);
            editText.setFocusable(editable);
            editText.setFocusableInTouchMode(editable);
            editText.setClickable(editable);
            editText.setLongClickable(editable);
            editText.setCursorVisible(editable);
        }
        if (editable && editTexts.length > 0) editTexts[0].requestFocus();
    }
}
