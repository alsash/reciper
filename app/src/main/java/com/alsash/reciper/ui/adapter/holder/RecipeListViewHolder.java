package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.RecipeListAdapter;
import com.alsash.reciper.ui.animator.FlipAnimatorHelper;

public class RecipeListViewHolder extends RecyclerView.ViewHolder {

    private final TextView frontTitle;
    private final ImageButton frontExpandButton;
    private final ImageButton frontOpenButton;
    private final ImageButton backExpandButton;
    private final ImageButton backOpenButton;

    private final FlipAnimatorHelper flipAnimator;

    public RecipeListViewHolder(View rootView) {
        super(rootView);

        FrameLayout cardRecipe = (FrameLayout) rootView.findViewById(R.id.card_recipe);
        flipAnimator = new FlipAnimatorHelper(cardRecipe);
        cardRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBackVisible = flipAnimator.flip();
                setIsRecyclable(!isBackVisible);
            }
        });

        frontTitle = (TextView) rootView.findViewById(R.id.card_recipe_front_title);
        frontExpandButton = (ImageButton) rootView.findViewById(R.id.card_recipe_front_expand_button);
        frontOpenButton = (ImageButton) rootView.findViewById(R.id.card_recipe_front_open_button);

        backExpandButton = (ImageButton) rootView.findViewById(R.id.card_recipe_back_expand_button);
        backOpenButton = (ImageButton) rootView.findViewById(R.id.card_recipe_back_open_button);
    }

    public void bindRecipe(final Recipe recipe, final RecipeListAdapter.OnRecipeInteraction interacts) {

        View.OnClickListener openListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interacts.click(recipe);
            }
        };
        View.OnClickListener expandListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interacts.expand(recipe);
            }
        };

        frontExpandButton.setOnClickListener(expandListener);
        frontOpenButton.setOnClickListener(openListener);

        backExpandButton.setOnClickListener(expandListener);
        backOpenButton.setOnClickListener(openListener);

        frontTitle.setText("Very dfgdksg;s lkms;lgk ms;lkgm;slkbm; dlkfgbm ;lkm ;lkm;dfg");
    }

    private void setBackgroundTint() {

    }
}
