package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeListViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListViewHolder> {

    private final List<Recipe> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;

    public RecipeListAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add(new RecipeImpl(i));
        }
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
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

}
