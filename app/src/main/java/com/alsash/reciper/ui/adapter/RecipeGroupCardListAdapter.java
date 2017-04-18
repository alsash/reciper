package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.RecipeGroup;
import com.alsash.reciper.mvp.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.adapter.holder.RecipeGroupCardHolder;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecipeGroupCardListAdapter
        extends RecyclerView.Adapter<RecipeGroupCardHolder> {

    private final WeakReference<RecipeListInteraction> interactionRef;
    private final List<? extends RecipeGroup> groups;

    public RecipeGroupCardListAdapter(RecipeListInteraction recipeInteraction,
                                      List<? extends RecipeGroup> groups) {
        this.interactionRef = new WeakReference<>(recipeInteraction);
        this.groups = groups;
    }

    @Override
    public RecipeGroupCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeGroupCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecipeGroupCardHolder holder, int position) {
        RecipeGroup group = groups.get(position);
        RecipeListInteraction interaction = interactionRef.get();
        if (interaction == null) return;
        holder.bindGroup(group, interaction);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
