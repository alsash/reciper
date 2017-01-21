package com.alsash.reciper.ui.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.reciper.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private final List<String> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;

    public RecipeListAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add("Recipe # " + i);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe_full, parent, false);
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
        void click();

        void expand();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        final TextView recipeName;
        final FloatingActionButton expandButton;

        public RecipeViewHolder(View rootView) {
            super(rootView);
            recipeName = (TextView) rootView.findViewById(R.id.recipe_name);
            expandButton = (FloatingActionButton) rootView.findViewById(R.id.recipe_expand);
        }

        public void bindRecipe(final String recipe, final OnRecipeInteraction recipeInteraction) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeInteraction.click();
                }
            });
            recipeName.setText(recipe);
            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeInteraction.expand();
                }
            });
        }
    }
}
