package com.alsash.reciper.ui.adapter.holder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Ingredient;

/**
 * A view that holds ingredient representation views
 */
public class IngredientHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView weight;
    private final TextView quantity;
    private final ImageButton expand;
    private final ConstraintLayout expandLayout;
    private final SeekBar weightBar;
    private final EditText weightEdit;
    private final ImageButton menu;

    public IngredientHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        name = (TextView) itemView.findViewById(R.id.ingredient_name);
        weight = (TextView) itemView.findViewById(R.id.ingredient_weight);
        quantity = (TextView) itemView.findViewById(R.id.ingredient_quantity);
        expand = (ImageButton) itemView.findViewById(R.id.ingredient_expand);
        expandLayout = (ConstraintLayout) itemView.findViewById(R.id.ingredient_expand_constraint);
        weightBar = (SeekBar) itemView.findViewById(R.id.ingredient_expand_weight_bar);
        weightEdit = (EditText) itemView.findViewById(R.id.ingredient_expand_weight_edit);
        menu = (ImageButton) itemView.findViewById(R.id.ingredient_expand_menu);
    }

    public void bindIngredient(Ingredient ingredient) {
        name.setText(ingredient.getName());
        String weightFull = String.valueOf((int) ingredient.getWeight()) + " "
                + ingredient.getWeightUnit();
        weight.setText(weightFull);
        quantity.setVisibility(View.GONE);
        weightBar.setProgress((int) ingredient.getWeight());
        weightBar.setMax((int) (ingredient.getWeight() * 2) + 1);
        weightEdit.setText(String.valueOf((int) ingredient.getWeight()));
    }

    public void setExpanded(final boolean expanded, boolean animate) {
        expand.clearAnimation();
        expandLayout.clearAnimation();
        float density = expandLayout.getContext().getResources().getDisplayMetrics().density;
        float[] rotate = new float[]{0f, -180f};
        float[] translateY = new float[]{-48 * density, 0f};

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(expand, "rotation",
                rotate[expanded ? 0 : 1], rotate[expanded ? 1 : 0]);
        ObjectAnimator collapseAnim = ObjectAnimator.ofFloat(expandLayout, "translationY",
                translateY[expanded ? 0 : 1], translateY[expanded ? 1 : 0]);

        collapseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translation = (float) animation.getAnimatedValue("translationY");
                expandLayout.getLayoutParams().height += translation;
                expandLayout.requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(animate ? 300 : 0);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!expanded) return;
                expandLayout.setVisibility(View.VISIBLE);
                weightEdit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (expanded) return;
                expandLayout.setVisibility(View.GONE);
                weightEdit.setVisibility(View.GONE);
            }
        });
        animatorSet.playTogether(rotationAnim, collapseAnim);
        animatorSet.start();
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. expandListener
     *                  1. menuListener
     */
    public void setListeners(View.OnClickListener... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    expand.setOnClickListener(listeners[i]);
                    break;
                default:
                    break;
            }
        }
    }
}
