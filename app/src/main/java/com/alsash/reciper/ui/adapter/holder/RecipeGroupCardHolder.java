package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.RecipeGroup;
import com.alsash.reciper.ui.adapter.RecipeSingleCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

public class RecipeGroupCardHolder extends RecyclerView.ViewHolder {

    private static final int INITIAL_PREFETCH_ITEMS = 4;

    private TextView groupTitle;
    private RecyclerView groupList;

    public RecipeGroupCardHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_group, parent, false));

        groupTitle = (TextView) itemView.findViewById(R.id.group_name);
        groupList = (RecyclerView) itemView.findViewById(R.id.group_list);
    }

    public void bindGroup(RecipeGroup group, RecipeListInteraction interaction) {
        groupTitle.setText(group.getName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(groupList.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(INITIAL_PREFETCH_ITEMS);
        groupList.setLayoutManager(layoutManager);
        groupList.setAdapter(new RecipeSingleCardListAdapter(interaction, group.getRecipes()));
        groupList.setItemAnimator(new FlipCardListAnimator());
    }
}
