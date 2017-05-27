package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.alsash.reciper.app.AppContract.Payload.FLIP_BACK_TO_FRONT;
import static com.alsash.reciper.app.AppContract.Payload.FLIP_FRONT_TO_BACK;

public class RecipeCardListAdapter extends RecyclerView.Adapter<RecipeCardHolder> {

    private final RecipeListInteraction interaction;
    private final List<? extends Recipe> recipeList;
    private final Set<Integer> backCardPositions;

    public RecipeCardListAdapter(RecipeListInteraction interaction,
                                 List<? extends Recipe> recipeList) {
        this.interaction = interaction;
        this.recipeList = recipeList;
        this.backCardPositions = new HashSet<>();
    }

    @Override
    public RecipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeCardHolder(parent);
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
                            notifyItemChanged(adapterPosition, FLIP_BACK_TO_FRONT);
                        } else {
                            backCardPositions.add(adapterPosition);
                            notifyItemChanged(adapterPosition, FLIP_FRONT_TO_BACK);
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
