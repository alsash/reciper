package com.alsash.reciper.ui.adapter.holder;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.ui.animator.ExpandAnimatorHelper;

/**
 * A view that holds ingredient representation views
 */
public class RecipeIngredientHolder extends RecyclerView.ViewHolder {
    private static final TimeInterpolator EXPAND_INTERPOLATOR
            = new AccelerateDecelerateInterpolator();
    private static final int EXPAND_DURATION_MS = 400;
    private static final int EXPAND_HEIGHT_DP = 180;

    private final EditText name;
    private final EditText weightValue;
    private final TextView weightUnit;
    private final ImageButton editButton;
    private final ImageButton expandButton;
    private final ConstraintLayout expandLayout;
    private final TextView foodName;
    private final TextView proteinValue;
    private final TextView fatValue;
    private final TextView carbsValue;
    private final TextView energyValue;

    public RecipeIngredientHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        name = (EditText) itemView.findViewById(R.id.item_ingredient_name);
        weightValue = (EditText) itemView.findViewById(R.id.item_ingredient_weight_value);
        weightUnit = (TextView) itemView.findViewById(R.id.item_ingredient_weight_unit);
        editButton = (ImageButton) itemView.findViewById(R.id.item_ingredient_edit);
        expandButton = (ImageButton) itemView.findViewById(R.id.item_ingredient_expand);
        foodName = (TextView) itemView.findViewById(R.id.item_food_name);
        proteinValue = (TextView) itemView.findViewById(R.id.item_food_protein);
        fatValue = (TextView) itemView.findViewById(R.id.item_food_fat);
        carbsValue = (TextView) itemView.findViewById(R.id.item_food_carbs);
        energyValue = (TextView) itemView.findViewById(R.id.item_food_energy);
        expandLayout = (ConstraintLayout) itemView.findViewById(R.id.item_food_expand_constraint);
    }

    public void bindEntity(Ingredient ingredient) {
        if (ingredient == null) {
            name.setText("");
            weightValue.setText("0");
            weightUnit.setText("");
        } else {
            name.setText(ingredient.getName());
            weightValue.setText(String.valueOf((int) Math.round(ingredient.getWeight())));
            weightUnit.setText(ingredient.getWeightUnit());
        }

        Food food = ingredient != null ? ingredient.getFood() : null;
        foodName.setText(food != null ? food.getName() : "");

        float p = food != null ? (float) food.getProtein() * 100 : 0;
        float f = food != null ? (float) food.getFat() * 100 : 0;
        float c = food != null ? (float) food.getCarbs() * 100 : 0;
        int e = food != null ? (int) Math.round(food.getEnergy() * 100) : 0;

        Context context = name.getContext();
        proteinValue.setText(context.getString(R.string.quantity_food_protein_gram, p));
        fatValue.setText(context.getString(R.string.quantity_food_fat_gram, f));
        carbsValue.setText(context.getString(R.string.quantity_food_carbs_gram, c));
        energyValue.setText(context.getString(R.string.quantity_food_energy_calorie, e));
    }

    public String getEditName() {
        return name.getText().toString();
    }

    public int getEditWeight() {
        try {
            return Math.round(Float.parseFloat(weightValue.getText().toString()));
        } catch (Throwable e) {
            weightValue.setText("0");
            return 0;
        }
    }

    public void setEditable(boolean editable) {
        editButton.setImageResource(editable ?
                R.drawable.edit_icon_orange :
                R.drawable.edit_icon_gray);
        weightValue.setEnabled(editable);
        weightValue.setFocusable(editable);
        weightValue.setFocusableInTouchMode(editable);
        weightValue.setClickable(editable);
        weightValue.setLongClickable(editable);
        name.setEnabled(editable);
        name.setFocusable(editable);
        name.setFocusableInTouchMode(editable);
        name.setClickable(editable);
        name.setLongClickable(editable);
        if (editable) weightValue.requestFocus();
    }

    public void setExpanded(boolean expanded, boolean animate) {
        ExpandAnimatorHelper.get()
                .expand(expanded)
                .interpolator(animate ? EXPAND_INTERPOLATOR : null)
                .durationMillis(animate ? EXPAND_DURATION_MS : 0)
                .translationDp(EXPAND_HEIGHT_DP)
                .button(expandButton)
                .layout(expandLayout)
                .start();
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. editValuesListener - must implement View.OnClickListener
     *                  1. expandListener     - must implement View.OnClickListener
     */
    public void setListeners(View.OnClickListener... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    editButton.setOnClickListener(listeners[i]);
                    break;
                case 1:
                    expandButton.setOnClickListener(listeners[i]);
                    break;
            }
        }
    }
}
