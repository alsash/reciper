package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;

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
        View frontView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe_front, parent, false);
        View backView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe_back, parent, false);
        return new RecipeViewHolder(frontView, backView);
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

    public static class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View frontView;
        final View backView;
        final TextView recipeTitle;
        final ImageButton expandButton;
        final ImageButton openButton;
        boolean isInFront;

        public RecipeViewHolder(View frontView, View backView) {
            super(frontView);
            recipeTitle = (TextView) frontView.findViewById(R.id.card_recipe_title);
            expandButton = (ImageButton) frontView.findViewById(R.id.card_recipe_expand_button);
            openButton = (ImageButton) frontView.findViewById(R.id.card_recipe_open_button);
            frontView.setOnClickListener(this);
            this.frontView = frontView;
            this.backView = backView;
            isInFront = true;
        }

        public void bindRecipe(final Recipe recipe, final OnRecipeInteraction recipeInteraction) {
            openButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeInteraction.click(recipe);
                }
            });
            recipeTitle.setText(recipe.getName());
            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeInteraction.expand(recipe);
                }
            });
        }

        /**
         * Flip recipe card animation
         *
         * @param v
         */
        @Override
        public void onClick(View v) {

        }
    }
}
