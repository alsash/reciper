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
 * Simple category selection holder
 */
public class EntityFoodSelectionHolder extends BaseEntitySelectionHolder {

    private static final TimeInterpolator EXPAND_INTERPOLATOR
            = new AccelerateDecelerateInterpolator();
    private static final int EXPAND_DURATION_MS = 400;
    private static final int EXPAND_HEIGHT_DP = 180;

    private final TextView name;
    private final EditText ndbno;
    private final TextView protein;
    private final TextView fat;
    private final TextView carbs;
    private final TextView energy;
    private final ImageButton expandButton;
    private final ConstraintLayout expandLayout;

    public EntityFoodSelectionHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId, -1);
        name = (TextView) itemView.findViewById(R.id.item_food_name);
        ndbno = (EditText) itemView.findViewById(R.id.item_food_ndbno);
        ndbno.setEnabled(false);
        ndbno.setFocusable(false);
        ndbno.setFocusableInTouchMode(false);
        ndbno.setClickable(false);
        ndbno.setLongClickable(false);
        protein = (TextView) itemView.findViewById(R.id.item_food_protein);
        fat = (TextView) itemView.findViewById(R.id.item_food_fat);
        carbs = (TextView) itemView.findViewById(R.id.item_food_carbs);
        energy = (TextView) itemView.findViewById(R.id.item_food_energy);
        expandButton = (ImageButton) itemView.findViewById(R.id.item_food_expand);
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

    public void setExpandListener(View.OnClickListener listener) {
        expandButton.setOnClickListener(listener);
    }
}
