package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.logic.unit.WeightUnit;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeDetailsIngredientsPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsIngredientsView;
import com.alsash.reciper.ui.adapter.IngredientListAdapter;
import com.alsash.reciper.ui.adapter.interaction.IngredientInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent recipe ingredients
 */
public class RecipeDetailsIngredientsFragment extends BaseFragment<RecipeDetailsIngredientsView>
        implements RecipeDetailsIngredientsView, IngredientInteraction {

    @Inject
    RecipeDetailsIngredientsPresenter presenter;
    @Inject
    NavigationLogic navigator;

    // Weight card
    private EditText weightValue;
    private TextView weightUnit;
    private ImageButton weightEdit;
    private boolean weightEditable = false;
    private View.OnClickListener weightEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            weightEditable = !weightEditable;
            setEditable(weightEditable, weightEdit, weightValue);
            if (weightEditable) return;
            presenter.requestWeightEdit(getThisView(),
                    weightValue.getText().toString());
        }
    };
    // Ingredients card
    private TextView ingredientsTitle;
    private ImageButton ingredientsAddButton;
    private RecyclerView ingredientsList;
    private IngredientListAdapter ingredientAdapter;
    private View.OnClickListener ingredientsAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.requestIngredientAdd(getThisView());
        }
    };
    private ItemTouchHelper.Callback ingredientsTouchCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN, ItemTouchHelper.START) {

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder source,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
            presenter.requestIngredientDelete(getThisView(), holder.getAdapterPosition());
        }
    };
    private ItemTouchHelper ingredientsTouchHelper = new ItemTouchHelper(ingredientsTouchCallback);

    public static RecipeDetailsIngredientsFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsIngredientsFragment(), intent);
    }

    @Override
    public void onEditValues(Ingredient ingredient, String name, int weight) {
        presenter.requestIngredientEdit(getThisView(), ingredient, name, weight);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        if (ingredientAdapter == null) {
            ingredientAdapter = new IngredientListAdapter(this, ingredients);
            if (ingredientsList != null)
                ingredientsList.setAdapter(ingredientAdapter);
        } else {
            ingredientAdapter.notifyDataSetChanged();
        }
        showTitle();
    }

    @Override
    public void showIngredientsAddDialog(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDialogIngredientView(recipe);
    }

    @Override
    public void showIngredientInsert(int position) {
        if (ingredientAdapter != null) {
            ingredientAdapter.notifyItemInserted(position);
            if (ingredientsList != null) ingredientsList.scrollToPosition(position);
        }
        showTitle();
    }

    @Override
    public void showIngredientDelete(int position) {
        if (ingredientAdapter != null)
            ingredientAdapter.notifyItemRemoved(position);
        showTitle();
    }

    @Override
    public void showIngredientDeleteMessage(String ingredientName, final MutableBoolean reject) {
        if (getView() == null) {
            reject.set(false);
            return;
        }
        Snackbar.make(getView(),
                getString(R.string.fragment_entity_list_delete_success, ingredientName),
                Snackbar.LENGTH_SHORT)
                .setAction(R.string.fragment_entity_list_delete_reject,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reject.set(true);
                            }
                        })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!reject.is()) reject.set(false);
                    }
                })
                .setActionTextColor(ResourcesCompat.getColor(getResources(),
                        R.color.orange_500, null))
                .show();
    }

    @Override
    public void showWeight(int weight, WeightUnit unit) {
        if (weightValue != null)
            weightValue.setText(String.valueOf(weight));
        if (weightUnit == null) return;
        switch (unit) {
            case GRAM:
                weightUnit.setText(getResources()
                        .getQuantityString(R.plurals.quantity_weight_gram_unit, weight));
                break;
            case KILOGRAM:
                weightUnit.setText(getResources()
                        .getQuantityString(R.plurals.quantity_weight_kilogram_unit, weight));
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup group,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_ingredients, group, false);

        // Weight and serving card
        weightValue = (EditText) layout.findViewById(R.id.recipe_weight_value);
        weightUnit = (TextView) layout.findViewById(R.id.recipe_weight_unit);
        weightEdit = (ImageButton) layout.findViewById(R.id.recipe_weight_edit);
        weightEdit.setOnClickListener(weightEditListener);
        setEditable(weightEditable, weightEdit, weightValue);

        // Ingredients card
        ingredientsTitle = (TextView) layout.findViewById(R.id.recipe_ingredients_title);
        ingredientsAddButton = (ImageButton) layout.findViewById(R.id.recipe_ingredients_add);
        ingredientsAddButton.setOnClickListener(ingredientsAddListener);
        ingredientsList = (RecyclerView) layout.findViewById(R.id.recipe_ingredients_list);
        ingredientsList.setNestedScrollingEnabled(false);
        if (ingredientAdapter != null) ingredientsList.setAdapter(ingredientAdapter);
        ingredientsList.addItemDecoration(new DividerItemDecoration(ingredientsList.getContext(),
                DividerItemDecoration.VERTICAL));
        ingredientsTouchHelper.attachToRecyclerView(ingredientsList);
        return layout;
    }

    @Override
    protected RecipeDetailsIngredientsPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }

    private void showTitle() {
        if (ingredientsTitle != null && ingredientAdapter != null) {
            int size = ingredientAdapter.getItemCount();
            ingredientsTitle.setText(getResources().getQuantityString(R.plurals.quantity_ingredient,
                    size, size));
        }
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
