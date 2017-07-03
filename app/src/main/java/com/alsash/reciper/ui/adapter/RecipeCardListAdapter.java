package com.alsash.reciper.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.adapter.observer.AdapterPositionSetObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.alsash.reciper.app.AppContract.Payload.FLIP_BACK_TO_FRONT;
import static com.alsash.reciper.app.AppContract.Payload.FLIP_FRONT_TO_BACK;

public class RecipeCardListAdapter extends RecyclerView.Adapter<RecipeCardHolder> {

    private final RecipeListInteraction interaction;
    private final List<? extends Recipe> recipeList;
    private final Set<Integer> backCardPositions;
    @LayoutRes
    private final int recipeCardLayoutId;

    public RecipeCardListAdapter(RecipeListInteraction interaction,
                                 List<? extends Recipe> recipeList,
                                 @LayoutRes int recipeCardLayoutId) {
        this.interaction = interaction;
        this.recipeList = recipeList;
        this.backCardPositions = new HashSet<>();
        this.recipeCardLayoutId = recipeCardLayoutId;
        setHasStableIds(true);
        registerAdapterDataObserver(new AdapterPositionSetObserver(backCardPositions));
    }

    @Override
    public RecipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeCardHolder(parent, recipeCardLayoutId);
    }

    @Override
    public long getItemId(int position) {
        return recipeList.get(position).getId();
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
                // Favorite Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Recipe recipe = recipeList.get(holder.getAdapterPosition());
                        holder.setFavorite(!recipe.isFavorite(), true);
                        interaction.onFavorite(recipe);
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
