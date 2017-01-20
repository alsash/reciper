package com.alsash.reciper.ui.adapter;

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

    public RecipeListAdapter() {
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
        holder.bindRecipe(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        final TextView recipeName;

        public RecipeViewHolder(View rootView) {
            super(rootView);
            recipeName = (TextView) rootView.findViewById(R.id.recipe_name);
        }

        public void bindRecipe(String recipe) {
            recipeName.setText(recipe);
        }
    }
}
