package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.animator.FlipAnimatorHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;

    public RecipeListAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add(new RecipeImpl(i));
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe_flip, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bindRecipe(recipeList.get(position), recipeInteraction);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface OnRecipeInteraction {
        void click(Recipe recipe);

        void expand(Recipe recipe);
    }

    private static class RecipeImpl implements Recipe {

        private final long id;

        public RecipeImpl(long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return "Recipe # " + id;
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        final FrameLayout cardRecipeFlip;
        final TextView frontTitle;
        final ImageButton frontExpandButton;
        final ImageButton frontOpenButton;
        final ImageButton backExpandButton;
        final ImageButton backOpenButton;

        final FlipAnimatorHelper flipAnimator;

        View.OnClickListener openListener;
        View.OnClickListener expandListener;

        public RecipeViewHolder(View rootView) {
            super(rootView);
            setIsRecyclable(true);

            cardRecipeFlip = (FrameLayout) rootView.findViewById(R.id.card_recipe_flip);
            flipAnimator = new FlipAnimatorHelper(cardRecipeFlip);
            cardRecipeFlip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipAnimator.flip();
                    setIsRecyclable(!isRecyclable());
                    flipBinding();
                }
            });

            frontTitle = (TextView) rootView.findViewById(R.id.card_recipe_front_title);
            frontExpandButton = (ImageButton) rootView.findViewById(R.id.card_recipe_front_expand_button);
            backExpandButton = (ImageButton) rootView.findViewById(R.id.card_recipe_back_expand_button);
            frontOpenButton = (ImageButton) rootView.findViewById(R.id.card_recipe_front_open_button);
            backOpenButton = (ImageButton) rootView.findViewById(R.id.card_recipe_back_open_button);
        }

        public void bindRecipe(final Recipe recipe, final OnRecipeInteraction interacts) {

            openListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interacts.click(recipe);
                }
            };
            expandListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interacts.expand(recipe);
                }
            };

            if (flipAnimator.isBackVisible()) {
                frontOpenButton.setOnClickListener(openListener);
                frontExpandButton.setOnClickListener(expandListener);
            } else {
                backOpenButton.setOnClickListener(openListener);
                backExpandButton.setOnClickListener(expandListener);
            }
            frontTitle.setText(recipe.getName());
        }

        private void flipBinding() {
            if (flipAnimator.isBackVisible()) {
                backOpenButton.setOnClickListener(openListener);
                backExpandButton.setOnClickListener(openListener);
                frontOpenButton.setOnClickListener(null);
                frontExpandButton.setOnClickListener(null);
            } else {
                backOpenButton.setOnClickListener(null);
                backExpandButton.setOnClickListener(null);
                frontOpenButton.setOnClickListener(openListener);
                frontExpandButton.setOnClickListener(expandListener);
            }
        }
    }
}
