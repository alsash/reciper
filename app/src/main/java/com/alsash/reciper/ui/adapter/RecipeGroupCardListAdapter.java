package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseRecipeGroup;
import com.alsash.reciper.ui.adapter.holder.RecipeGroupCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.adapter.interaction.RecipeSingleInteraction;

import java.util.List;

public class RecipeGroupCardListAdapter extends RecyclerView.Adapter<RecipeGroupCardHolder> {

    private final RecipeGroupInteraction groupInteraction;
    private final RecipeSingleInteraction singleInteraction;
    private final List<? extends BaseRecipeGroup> groups;

    public RecipeGroupCardListAdapter(List<? extends BaseRecipeGroup> groups,
                                      RecipeGroupInteraction groupInteraction,
                                      RecipeSingleInteraction singleInteraction) {
        this.groups = groups;
        this.groupInteraction = groupInteraction;
        this.singleInteraction = singleInteraction;
    }

    @Override
    public RecipeGroupCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeGroupCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecipeGroupCardHolder holder, int position) {
        BaseRecipeGroup group = groups.get(position);
        holder.bindGroup(group, groupInteraction, singleInteraction);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
