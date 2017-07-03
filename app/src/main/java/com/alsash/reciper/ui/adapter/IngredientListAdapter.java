package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.ui.adapter.holder.RecipeIngredientHolder;
import com.alsash.reciper.ui.adapter.interaction.IngredientInteraction;
import com.alsash.reciper.ui.adapter.observer.AdapterPositionSetObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A recipe ingredient list adapter
 */
public class IngredientListAdapter extends RecyclerView.Adapter<RecipeIngredientHolder> {

    private final IngredientInteraction interaction;
    private final List<Ingredient> ingredients;
    private final Set<Integer> expandPositions;

    public IngredientListAdapter(IngredientInteraction interaction, List<Ingredient> ingredients) {
        this.interaction = interaction;
        this.ingredients = ingredients;
        this.expandPositions = new HashSet<>();
        registerAdapterDataObserver(new AdapterPositionSetObserver(expandPositions));
    }

    @Override
    public RecipeIngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeIngredientHolder(parent, R.layout.item_ingredient);
    }

    @Override
    public void onBindViewHolder(final RecipeIngredientHolder holder, int position) {
        holder.bindIngredient(ingredients.get(position));
        holder.setExpanded(expandPositions.contains(position), false);
        holder.setListeners(
                // Expand listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        boolean expanded = !expandPositions.remove(position)
                                && expandPositions.add(position);
                        holder.setExpanded(expanded, true);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
