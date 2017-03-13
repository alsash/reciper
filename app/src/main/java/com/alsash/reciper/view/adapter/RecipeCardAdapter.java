package com.alsash.reciper.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.adapter.holder.RecipeCardHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.alsash.reciper.view.contract.KeyContract.PAYLOAD_FLIP_BACK_TO_FRONT;
import static com.alsash.reciper.view.contract.KeyContract.PAYLOAD_FLIP_FRONT_TO_BACK;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardHolder> {

    private final RecipeListInteraction recipeInteraction;
    private final List<Recipe> recipeList;
    private final Set<Integer> backCardPositions;

    public RecipeCardAdapter(RecipeListInteraction recipeInteraction, List<Recipe> recipeList) {
        this.recipeInteraction = recipeInteraction;
        this.recipeList = recipeList;
        this.backCardPositions = new HashSet<>();
    }

    @Override
    public RecipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);
        return new RecipeCardHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RecipeCardHolder holder, int position) {

        holder.bindRecipe(recipeList.get(position));
        holder.setBackVisible(backCardPositions.contains(position));

        holder.setListeners(
                // Flip Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Flip animation. Stage 1 of 3. Notify observers about flip is triggered.
                        int adapterPosition = holder.getAdapterPosition();
                        if (backCardPositions.contains(adapterPosition)) {
                            backCardPositions.remove(adapterPosition);
                            notifyItemChanged(adapterPosition, PAYLOAD_FLIP_BACK_TO_FRONT);
                        } else {
                            backCardPositions.add(adapterPosition);
                            notifyItemChanged(adapterPosition, PAYLOAD_FLIP_FRONT_TO_BACK);
                        }
                    }
                },
                // Expand Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        Recipe recipe = recipeList.get(adapterPosition);
                        recipeInteraction.onExpand(recipe, adapterPosition);
                    }
                },
                // Open Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        Recipe recipe = recipeList.get(adapterPosition);
                        recipeInteraction.onOpen(recipe, adapterPosition);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
