package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * An view that holds the Recipe card,
 * response to flip animations
 * and send interaction events ro receiver
 */
public class RecipeCardHolder extends RecyclerView.ViewHolder {

    private final CardView frontCard;
    private final ImageView frontImage;
    private final ProgressBar frontImageBar;
    private final TextView frontTitle;
    private final ImageButton frontPinButton;
    private final ImageButton frontOpenButton;

    private final CardView backCard;
    private final ImageView backAccountImage;
    private final TextView backAccountTitle;
    private final TextView backCreationDate;
    private final TextView backTitle;
    private final ImageButton backPinButton;
    private final ImageButton backOpenButton;

    public RecipeCardHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false));
        frontCard = (CardView) itemView.findViewById(R.id.card_recipe_front);
        frontImage = (ImageView) itemView.findViewById(R.id.card_recipe_front_image);
        frontImageBar = (ProgressBar) itemView.findViewById(R.id.card_recipe_front_image_bar);
        frontTitle = (TextView) itemView.findViewById(R.id.card_recipe_front_title);
        frontPinButton = (ImageButton) itemView.findViewById(R.id.card_recipe_front_expand_button);
        frontOpenButton = (ImageButton) itemView.findViewById(R.id.card_recipe_front_open_button);

        backCard = (CardView) itemView.findViewById(R.id.card_recipe_back);
        backAccountImage = (ImageView) itemView.findViewById(R.id.card_recipe_back_account_image);

        backAccountTitle = (TextView) itemView.findViewById(R.id.card_recipe_back_account_title);
        backCreationDate = (TextView) itemView.findViewById(R.id.card_recipe_back_recipe_creation_date);
        backTitle = (TextView) itemView.findViewById(R.id.card_recipe_back_title);
        backPinButton = (ImageButton) itemView.findViewById(R.id.card_recipe_back_expand_button);
        backOpenButton = (ImageButton) itemView.findViewById(R.id.card_recipe_back_open_button);

        setBackVisible(false);
    }

    public void bindRecipe(Recipe recipe) {
        frontTitle.setText(recipe.getName());

        ImageLoader.getInstance().load(recipe.getMainPhoto(), frontImage, frontImageBar);

        backTitle.setText(recipe.getName());

        ImageLoader.getInstance().load(recipe.getAuthor().getPhoto(), backAccountImage);

    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. flipListener
     *                  1. PinListener
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
                    backPinButton.setOnClickListener(listeners[i]);
                    frontPinButton.setOnClickListener(listeners[i]);
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
