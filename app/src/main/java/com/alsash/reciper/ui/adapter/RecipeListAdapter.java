package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeBackCardHolder;
import com.alsash.reciper.ui.adapter.holder.RecipeFrontCardHolder;
import com.alsash.reciper.ui.adapter.holder.RecipeListCardHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListCardHolder> {

    public static final String PAYLOAD_FLIP = "com.alsash.reciper.ui.adapter.payload_flip";

    private final List<Recipe> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;
    private final Set<Integer> recipeBackCardViews = new HashSet<>();

    public RecipeListAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add(new RecipeImpl(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (recipeBackCardViews.contains(position)) {
            return R.layout.card_recipe_back;
        } else {
            return R.layout.card_recipe_front;
        }
    }

    @Override
    public RecipeListCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.card_recipe_front:
                return new RecipeFrontCardHolder(itemView);
            case R.layout.card_recipe_back:
                return new RecipeBackCardHolder(itemView);
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecipeListCardHolder holder, int position) {

        holder.bindRecipe(recipeList.get(position));
        holder.setListeners(
                // Flip Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.isFlipNeed = true;
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                },
                // Expand Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recipeInteraction.expand(recipeList.get(holder.getAdapterPosition()));
                    }
                },
                // Open Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recipeInteraction.open(recipeList.get(holder.getAdapterPosition()));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface OnRecipeInteraction {

        void open(Recipe recipe);

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
