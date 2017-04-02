package com.alsash.reciper.view.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.presenter.entity.Recipe;

public class RecipeCardHolder extends RecyclerView.ViewHolder {

    private final CardView frontCard;
    private final ImageView frontImage;
    private final TextView frontTitle;
    private final ImageButton frontExpandButton;
    private final ImageButton frontOpenButton;

    private final CardView backCard;
    private final ImageView backAccountImage;
    private final TextView backAccountTitle;
    private final TextView backCreationDate;
    private final TextView backTitle;
    private final ImageButton backExpandButton;
    private final ImageButton backOpenButton;

    public RecipeCardHolder(View rootView) {
        super(rootView);

        frontCard = (CardView) rootView.findViewById(R.id.card_recipe_front);
        frontImage = (ImageView) rootView.findViewById(R.id.card_recipe_front_image);
        frontTitle = (TextView) rootView.findViewById(R.id.card_recipe_front_title);
        frontExpandButton = (ImageButton) rootView.findViewById(R.id.card_recipe_front_expand_button);
        frontOpenButton = (ImageButton) rootView.findViewById(R.id.card_recipe_front_open_button);

        backCard = (CardView) rootView.findViewById(R.id.card_recipe_back);
        backAccountImage = (ImageView) rootView.findViewById(R.id.card_recipe_back_account_image);

        backAccountTitle = (TextView) rootView.findViewById(R.id.card_recipe_back_account_title);
        backCreationDate = (TextView) rootView.findViewById(R.id.card_recipe_back_recipe_creation_date);
        backTitle = (TextView) rootView.findViewById(R.id.card_recipe_back_title);
        backExpandButton = (ImageButton) rootView.findViewById(R.id.card_recipe_back_expand_button);
        backOpenButton = (ImageButton) rootView.findViewById(R.id.card_recipe_back_open_button);

        setBackVisible(false);
    }

    public void bindRecipe(Recipe recipe) {
        frontTitle.setText(recipe.getName());
        backTitle.setText(recipe.getName());
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. flipListener
     *                  1. expandListener
     *                  2. openListener
     */
    public void setListeners(View.OnClickListener... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    backCard.setOnClickListener(listeners[i]);
                    frontCard.setOnClickListener(listeners[i]);
                    break;
                case 1:
                    backExpandButton.setOnClickListener(listeners[i]);
                    frontExpandButton.setOnClickListener(listeners[i]);
                    break;
                case 2:
                    backOpenButton.setOnClickListener(listeners[i]);
                    frontOpenButton.setOnClickListener(listeners[i]);
                default:
                    break;
            }
        }
    }

    public void setBackVisible(boolean backVisible) {
        if (backVisible) {
            frontCard.setVisibility(View.GONE);
            backCard.setVisibility(View.VISIBLE);
        } else {
            frontCard.setVisibility(View.VISIBLE);
            backCard.setVisibility(View.GONE);
        }
        // Animations not always ends correctly. Here is the "medicine"
        frontCard.setAlpha(1.0f);
        frontCard.setRotationY(0.0f);
        backCard.setAlpha(1.0f);
        backCard.setRotationY(0.0f);
    }
}
