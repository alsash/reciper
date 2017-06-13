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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * An view that holds the Recipe card,
 * response to flip animations
 * and send interaction events ro receiver
 */
public class RecipeCardHolder extends RecyclerView.ViewHolder {

    private final CardView frontCard;
    private final ImageView frontImage;
    private final ProgressBar frontBar;
    private final TextView frontName;
    private final ImageButton frontFavButton;
    private final ImageButton frontFlipButton;

    private final CardView backCard;
    private final ImageView backAuthorImage;
    private final TextView backAuthorName;
    private final TextView backSource;
    private final TextView backDate;
    private final TextView backDescription;
    private final ImageButton backFavButton;
    private final ImageButton backFlipButton;

    private final SimpleDateFormat dateFormat;

    public RecipeCardHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_card, parent, false));

        frontCard = (CardView) itemView.findViewById(R.id.item_recipe_card_front);
        frontImage = (ImageView) itemView.findViewById(R.id.item_recipe_front_image);
        frontBar = (ProgressBar) itemView.findViewById(R.id.item_recipe_front_image_bar);
        frontName = (TextView) itemView.findViewById(R.id.item_recipe_front_name);
        frontFavButton = (ImageButton) itemView.findViewById(R.id.item_recipe_front_bt_fav);
        frontFlipButton = (ImageButton) itemView.findViewById(R.id.item_recipe_front_bt_flip);

        backCard = (CardView) itemView.findViewById(R.id.item_recipe_card_back);
        backAuthorImage = (ImageView) itemView.findViewById(R.id.item_recipe_back_author_image);
        backAuthorName = (TextView) itemView.findViewById(R.id.item_recipe_back_author_name);
        backSource = (TextView) itemView.findViewById(R.id.item_recipe_back_source);
        backDate = (TextView) itemView.findViewById(R.id.item_recipe_back_date);
        backDescription = (TextView) itemView.findViewById(R.id.item_recipe_back_description);
        backFavButton = (ImageButton) itemView.findViewById(R.id.item_recipe_back_bt_fav);
        backFlipButton = (ImageButton) itemView.findViewById(R.id.item_recipe_back_bt_flip);

        dateFormat = new SimpleDateFormat(
                parent.getResources().getString(R.string.recipe_card_date_format),
                Locale.getDefault()
        );

        setBackVisible(false);
    }

    public void bindRecipe(Recipe recipe) {
        frontName.setText(recipe.getName());
        ImageLoader.getInstance().load(recipe.getMainPhoto(), frontImage, frontBar);
        ImageLoader.getInstance().load(recipe.getAuthor().getPhoto(), backAuthorImage);
        backAuthorName.setText(recipe.getAuthor().getName());
        backSource.setText(recipe.getSource());
        backDate.setText(dateFormat.format(recipe.getCreatedAt()));
        backDescription.setText(recipe.getDescription());
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. flipListener
     *                  1. FavoriteListener
     *                  2. openListener
     */
    public void setListeners(View.OnClickListener... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    backFlipButton.setOnClickListener(listeners[i]);
                    frontFlipButton.setOnClickListener(listeners[i]);
                case 1:
                    backFavButton.setOnClickListener(listeners[i]);
                    frontFavButton.setOnClickListener(listeners[i]);
                    break;
                case 2:
                    backCard.setOnClickListener(listeners[i]);
                    frontCard.setOnClickListener(listeners[i]);
                    break;
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
