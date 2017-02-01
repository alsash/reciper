package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;


public class RecipeBackCardHolder extends RecipeListCardHolder {

    private final CardView card;
    private final ImageView accountImage;
    private final TextView accountTitle;
    private final TextView creationDate;
    private final TextView title;
    private final ImageButton expandBtn;
    private final ImageButton openBtn;

    public RecipeBackCardHolder(View rootView) {
        super(rootView);
        card = (CardView) rootView.findViewById(R.id.card_recipe_back);
        accountImage = (ImageView) rootView.findViewById(R.id.card_recipe_back_account_image);
        accountTitle = (TextView) rootView.findViewById(R.id.card_recipe_back_account_title);
        creationDate = (TextView) rootView.findViewById(R.id.card_recipe_back_recipe_creation_date);
        title = (TextView) rootView.findViewById(R.id.card_recipe_back_title);
        expandBtn = (ImageButton) rootView.findViewById(R.id.card_recipe_back_expand_button);
        openBtn = (ImageButton) rootView.findViewById(R.id.card_recipe_back_open_button);
    }

    @Override
    public void bindRecipe(Recipe recipe) {

    }

    @Override
    public void setListeners(View.OnClickListener... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    card.setOnClickListener(listeners[i]);
                    break;
                case 1:
                    expandBtn.setOnClickListener(listeners[i]);
                    break;
                case 2:
                    openBtn.setOnClickListener(listeners[i]);
                default:
                    break;
            }
        }
    }
}
