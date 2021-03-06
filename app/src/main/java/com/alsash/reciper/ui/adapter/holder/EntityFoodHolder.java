package com.alsash.reciper.ui.adapter.holder;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.ui.animator.ExpandAnimatorHelper;

/**
 * A Food item holder
 */
public class EntityFoodHolder extends BaseEntityHolder {

    private static final TimeInterpolator EXPAND_INTERPOLATOR
            = new AccelerateDecelerateInterpolator();
    private static final int EXPAND_DURATION_MS = 400;
    private static final int EXPAND_HEIGHT_DP = 180;

    private final EditText name;
    private final EditText ndbno;
    private final TextView protein;
    private final TextView fat;
    private final TextView carbs;
    private final TextView energy;
    private final ImageButton valueEdit;
    private final ImageButton expand;
    private final ConstraintLayout expandLayout;

    public EntityFoodHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId);
        name = (EditText) itemView.findViewById(R.id.item_food_name);
        ndbno = (EditText) itemView.findViewById(R.id.item_food_ndbno);
        protein = (TextView) itemView.findViewById(R.id.item_food_protein);
        fat = (TextView) itemView.findViewById(R.id.item_food_fat);
        carbs = (TextView) itemView.findViewById(R.id.item_food_carbs);
        energy = (TextView) itemView.findViewById(R.id.item_food_energy);
        valueEdit = (ImageButton) itemView.findViewById(R.id.item_food_edit);
        expand = (ImageButton) itemView.findViewById(R.id.item_food_expand);
        expandLayout = (ConstraintLayout) itemView.findViewById(R.id.item_food_expand_constraint);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Food food = (Food) entity;
        Context context = name.getContext();
        name.setText(food.getName());
        ndbno.setText(food.getNdbNo());
        protein.setText(context.getString(R.string.quantity_food_protein_gram,
                (float) food.getProtein() * 100));
        fat.setText(context.getString(R.string.quantity_food_fat_gram,
                (float) food.getFat() * 100));
        carbs.setText(context.getString(R.string.quantity_food_carbs_gram,
                (float) food.getCarbs() * 100));
        energy.setText(context.getString(R.string.quantity_food_energy_calorie,
                (int) Math.round(food.getEnergy() * 100)));
    }

    @Override
    public String[] getEditable() {
        return new String[]{
                name.getText().toString(),
                ndbno.getText().toString()
        };
    }

    @Override
    public void setEditable(boolean editable) {
        valueEdit.setImageResource(editable ?
                R.drawable.edit_icon_orange :
                R.drawable.edit_icon_gray);
        ndbno.setEnabled(editable);
        ndbno.setFocusable(editable);
        ndbno.setFocusableInTouchMode(editable);
        ndbno.setClickable(editable);
        ndbno.setLongClickable(editable);
        name.setEnabled(editable);
        name.setFocusable(editable);
        name.setFocusableInTouchMode(editable);
        name.setClickable(editable);
        name.setLongClickable(editable);
        if (editable) name.requestFocus();
    }

    public void setExpanded(boolean expanded, boolean animate) {
        ExpandAnimatorHelper.get()
                .expand(expanded)
                .interpolator(animate ? EXPAND_INTERPOLATOR : null)
                .durationMillis(animate ? EXPAND_DURATION_MS : 0)
                .translationDp(EXPAND_HEIGHT_DP)
                .button(expand)
                .layout(expandLayout)
                .start();
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. editValuesListener - must implement View.OnClickListener
     *                  1. expandListener     - must implement View.OnClickListener
     */
    @Override
    public void setListeners(Object... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    valueEdit.setOnClickListener((View.OnClickListener) listeners[i]);
                    break;
                case 1:
                    expand.setOnClickListener((View.OnClickListener) listeners[i]);
                    break;
            }
        }
    }
}
