package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.ui.adapter.holder.IngredientHolder;
import com.alsash.reciper.ui.adapter.interaction.IngredientInteraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An Ingredient list adapter
 */
public class IngredientListAdapter extends RecyclerView.Adapter<IngredientHolder> {

    private final IngredientInteraction interaction;
    private final List<Ingredient> ingredients;
    private final Set<Integer> expendedPositions;

    public IngredientListAdapter(IngredientInteraction interaction, List<Ingredient> ingredients) {
        this.interaction = interaction;
        this.ingredients = ingredients;
        this.expendedPositions = new HashSet<>();
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientHolder(parent, R.layout.item_ingredient);
    }

    @Override
    public void onBindViewHolder(final IngredientHolder holder, int position) {
        holder.bindIngredient(ingredients.get(position));
        holder.setExpanded(expendedPositions.contains(position), false);
        holder.setListeners(
                // Expand listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        boolean expanded = !expendedPositions.remove(position)
                                && expendedPositions.add(position);
                        holder.setExpanded(expanded, true);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
