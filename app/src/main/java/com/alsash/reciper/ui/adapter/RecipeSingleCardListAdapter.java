package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeSingleCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.alsash.reciper.ui.contract.KeyContract.PAYLOAD_FLIP_BACK_TO_FRONT;
import static com.alsash.reciper.ui.contract.KeyContract.PAYLOAD_FLIP_FRONT_TO_BACK;

public class RecipeSingleCardListAdapter extends RecyclerView.Adapter<RecipeSingleCardHolder> {

    private final RecipeListInteraction interaction;
    private final List<? extends Recipe> recipeList;
    private final Set<Integer> backCardPositions;

    public RecipeSingleCardListAdapter(RecipeListInteraction interaction,
                                       List<? extends Recipe> recipeList) {
        this.interaction = interaction;
        this.recipeList = recipeList;
        this.backCardPositions = new HashSet<>();
    }

    @Override
    public RecipeSingleCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeSingleCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(final RecipeSingleCardHolder holder, int position) {
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
                        interaction.onExpand(recipeList.get(holder.getAdapterPosition()));
                    }
                },
                // Open Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        interaction.onOpen(recipeList.get(holder.getAdapterPosition()));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
