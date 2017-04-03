package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.RecipeGroup;
import com.alsash.reciper.mvp.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.adapter.holder.RecipeGroupCardHolder;

import java.util.List;

public class RecipeGroupCardListAdapter extends RecyclerView.Adapter<RecipeGroupCardHolder> {

    private RecipeListInteraction recipeInteraction;
    private List<RecipeGroup> groups;

    public RecipeGroupCardListAdapter(RecipeListInteraction recipeInteraction,
                                      List<RecipeGroup> groups) {
        this.recipeInteraction = recipeInteraction;
        this.groups = groups;
    }

    @Override
    public RecipeGroupCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeGroupCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecipeGroupCardHolder holder, int position) {
        RecipeGroup group = groups.get(position);
        holder.bindGroup(group, recipeInteraction);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
