package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeFlipperHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeFlipperHolder>
        implements RecipeFlipperHolder.OnFlipListener {

    private final List<Recipe> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;
    private final Set<Long> flippedRecipes = new HashSet<>();

    public RecipeListAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add(new RecipeImpl(i));
        }
    }

    @Override
    public void onFlip(boolean isBackVisible, int adapterPosition) {
        if (isBackVisible) {
            flippedRecipes.add(recipeList.get(adapterPosition).getId());
        } else {
            flippedRecipes.remove(recipeList.get(adapterPosition).getId());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (flippedRecipes.contains(recipeList.get(position).getId())) {
            return R.id.card_recipe_back;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecipeFlipperHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);

        boolean isBackVisible = (viewType == R.id.card_recipe_back);
        return new RecipeFlipperHolder(view, isBackVisible);
    }

    @Override
    public void onBindViewHolder(RecipeFlipperHolder holder, int position) {
        holder.bindRecipe(recipeList.get(position), recipeInteraction, this);
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
}
