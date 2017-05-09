package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseRecipeGroup;
import com.alsash.reciper.ui.adapter.holder.RecipeGroupCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.List;

public class RecipeGroupCardListAdapter extends RecyclerView.Adapter<RecipeGroupCardHolder> {

    private final RecipeListInteraction interaction;
    private final List<? extends BaseRecipeGroup> groups;

    public RecipeGroupCardListAdapter(List<? extends BaseRecipeGroup> groups,
                                      RecipeListInteraction recipeInteraction) {
        this.groups = groups;
        this.interaction = recipeInteraction;
    }

    @Override
    public RecipeGroupCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeGroupCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecipeGroupCardHolder holder, int position) {
        BaseRecipeGroup group = groups.get(position);
        holder.bindGroup(group, interaction);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
