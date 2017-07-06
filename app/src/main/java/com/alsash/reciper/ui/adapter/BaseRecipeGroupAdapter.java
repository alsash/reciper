package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.ui.adapter.holder.BaseRecipeGroupHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;

import java.util.List;

/**
 * An abstract adapter, that make view holders for the inner recipe lists
 *
 * @param <G>  - outer group model, that related to recipes
 * @param <VH> - outer group view holder
 */
public abstract class BaseRecipeGroupAdapter<G extends BaseEntity,
        VH extends BaseRecipeGroupHolder<G>> extends RecyclerView.Adapter<VH> {

    private final List<G> groupList;
    private final RecipeGroupInteraction<G> groupInteraction;

    public BaseRecipeGroupAdapter(List<G> groupList,
                                  RecipeGroupInteraction<G> groupInteraction) {
        this.groupList = groupList;
        this.groupInteraction = groupInteraction;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        G group = groupList.get(position);
        holder.bindGroup(group);
        holder.setInteraction(groupInteraction);
        holder.setPresenter(groupInteraction.injectInnerPresenter(group));
        holder.setListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupInteraction.onOpen(groupList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        holder.setViewVisible(true);
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        holder.setViewVisible(false);
    }
}
