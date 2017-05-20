package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.ui.adapter.holder.RecipeGroupCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeCardListInteraction;

import java.util.List;

public class RecipeGroupCardListAdapter extends RecyclerView.Adapter<RecipeGroupCardHolder> {

    private final RecipeCardListInteraction singleInteraction;
    private final List<? extends BaseEntity> groups;

    public RecipeGroupCardListAdapter(List<? extends BaseEntity> groups,
                                      RecipeCardListInteraction singleInteraction) {
        this.groups = groups;
        this.singleInteraction = singleInteraction;
    }

    @Override
    public RecipeGroupCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecipeGroupCardHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecipeGroupCardHolder holder, int position) {
        BaseEntity group = groups.get(position);
        holder.bindGroup(group, singleInteraction);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
