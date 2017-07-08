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
    private final Set<Integer> editablePositions;
    private final Set<Integer> expandedPositions;

    public IngredientListAdapter(IngredientInteraction interaction, List<Ingredient> ingredients) {
        this.interaction = interaction;
        this.ingredients = ingredients;
        this.editablePositions = new HashSet<>();
        this.expandedPositions = new HashSet<>();
        registerAdapterDataObserver(new AdapterPositionSetObserver(
                editablePositions,
                expandedPositions));
    }

    @Override
    public RecipeIngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeIngredientHolder(parent, R.layout.item_ingredient);
    }

    @Override
    public void onBindViewHolder(final RecipeIngredientHolder holder, int position) {
        holder.bindEntity(ingredients.get(position));
        holder.setEditable(editablePositions.contains(position));
        holder.setExpanded(expandedPositions.contains(position), false);
        holder.setListeners(
                // Edit listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        boolean editable = !editablePositions.remove(position)
                                && editablePositions.add(position);
                        holder.setEditable(editable);
                        if (!editable) {
                            interaction.onEditValues(ingredients.get(position),
                                    holder.getEditName(),
                                    holder.getEditWeight());
                        }
                    }
                },
                // Expand listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        boolean expanded = !expandedPositions.remove(position)
                                && expandedPositions.add(position);
                        holder.setExpanded(expanded, true);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
