package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.RecipeListAdapter;
import com.alsash.reciper.ui.animator.FlipAnimatorHelper;

public class RecipeFlipperHolder extends RecyclerView.ViewHolder {

    private final FrameLayout cardRecipe;
    private final FlipAnimatorHelper flipAnimator;
    private final boolean startBackVisible;

    private final ImageView frontRecipeImage;
    private final TextView frontRecipeTitle;
    private final ImageButton frontExpandButton;
    private final ImageButton frontOpenButton;

    private final ImageView backAccountImage;
    private final TextView backAccountTitle;
    private final TextView backRecipeCreationDate;
    private final TextView backRecipeTitle;
    private final ImageButton backExpandButton;
    private final ImageButton backOpenButton;

    public RecipeFlipperHolder(View rootView, boolean isBackVisible) {
        super(rootView);
        this.startBackVisible = isBackVisible;
        // Root binding
        cardRecipe = (FrameLayout) rootView.findViewById(R.id.card_recipe);
        flipAnimator = new FlipAnimatorHelper(cardRecipe, startBackVisible);
        CardView frontView = (CardView) rootView.findViewById(R.id.card_recipe_front);
        CardView backView = (CardView) rootView.findViewById(R.id.card_recipe_back);

        // Front binding
        frontRecipeImage = (ImageView) frontView.findViewById(R.id.recipe_image);
        frontRecipeTitle = (TextView) frontView.findViewById(R.id.recipe_title);
        frontExpandButton = (ImageButton) frontView.findViewById(R.id.expand_button);
        frontOpenButton = (ImageButton) frontView.findViewById(R.id.open_button);

        // Back binding
        backAccountImage = (ImageView) backView.findViewById(R.id.account_image);
        backAccountTitle = (TextView) backView.findViewById(R.id.account_title);
        backRecipeCreationDate = (TextView) backView.findViewById(R.id.recipe_creation_date);
        backRecipeTitle = (TextView) backView.findViewById(R.id.recipe_title);
        backExpandButton = (ImageButton) backView.findViewById(R.id.expand_button);
        backOpenButton = (ImageButton) backView.findViewById(R.id.open_button);
    }

    public void bindRecipe(final Recipe recipe, final RecipeListAdapter.OnRecipeInteraction interacts,
                           final OnFlipListener flipListener) {

        // Prepare listeners
        View.OnClickListener clickFlipListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flip(flipListener);
            }
        };
        View.OnClickListener expandListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interacts.expand(recipe);
            }
        };
        View.OnClickListener openListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interacts.click(recipe);
            }
        };

        // Bindings to root view
        cardRecipe.setOnClickListener(clickFlipListener);

        // Bindings to front view
        frontRecipeTitle.setText(recipe.getName());
        // frontRecipeImage.setImageBitmap(recipe.getImage(frontRecipeImage.getX(), frontRecipeImage.getY()));
        frontExpandButton.setOnClickListener(expandListener);
        frontOpenButton.setOnClickListener(openListener);

        // Bindings to back view
        // Account account = recipe.getAccount();
        // backAccountImage.setImageBitmap(account.getImage(backAccountImage.getX(), backAccountImage.getY()));
        // backAccountTitle.setText(account.getName());
        // backRecipeCreationDate.setText(recipe.getCreationDate());
        backRecipeTitle.setText(recipe.getName());
        backExpandButton.setOnClickListener(expandListener);
        backOpenButton.setOnClickListener(openListener);
    }

    private void flip(OnFlipListener listener) {
        boolean isBackVisible = flipAnimator.flip();
        boolean isRecyclable = (startBackVisible == isBackVisible);
        setIsRecyclable(isRecyclable);
        listener.onFlip(isBackVisible, getAdapterPosition());
    }

    public interface OnFlipListener {
        void onFlip(boolean isBackVisible, int adapterPosition);
    }

}
