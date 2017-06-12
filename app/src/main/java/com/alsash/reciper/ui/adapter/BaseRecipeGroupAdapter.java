package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.ui.adapter.holder.BaseRecipeGroupHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

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
    private final RecipeListInteraction recipeInteraction;

    public BaseRecipeGroupAdapter(List<G> groupList,
                                  RecipeGroupInteraction<G> groupInteraction,
                                  RecipeListInteraction recipeInteraction) {
        this.groupList = groupList;
        this.groupInteraction = groupInteraction;
        this.recipeInteraction = recipeInteraction;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        G group = groupList.get(position);
        holder.bindGroup(group);
        holder.setInteraction(recipeInteraction);
        holder.setPresenter(groupInteraction.injectInnerPresenter(group));
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
