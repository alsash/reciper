package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.Recipe;
import com.alsash.reciper.mvp.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.adapter.holder.RecipeSIngleCardHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.alsash.reciper.ui.contract.KeyContract.PAYLOAD_FLIP_BACK_TO_FRONT;
import static com.alsash.reciper.ui.contract.KeyContract.PAYLOAD_FLIP_FRONT_TO_BACK;

public class RecipeSingleCardListAdapter extends RecyclerView.Adapter<RecipeSIngleCardHolder> {

    private final RecipeListInteraction recipeInteraction;
    private final List<Recipe> recipeList;
    private final Set<Integer> backCardPositions;

    public RecipeSingleCardListAdapter(RecipeListInteraction recipeInteraction, List<Recipe> recipeList) {
        this.recipeInteraction = recipeInteraction;
        this.recipeList = recipeList;
        this.backCardPositions = new HashSet<>();
    }

    @Override
    public RecipeSIngleCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeSIngleCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(final RecipeSIngleCardHolder holder, int position) {

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
