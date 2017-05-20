package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseRecipeGroup;
import com.alsash.reciper.ui.adapter.RecipeSingleCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeSingleInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

public class RecipeGroupCardHolder extends RecyclerView.ViewHolder {

    private TextView groupTitle;
    private RecyclerView groupList;
    private LinearLayoutManager layoutManager;
    private RecipeSingleCardListAdapter adapter;
    private FlipCardListAnimator animator;

    public RecipeGroupCardHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_group, parent, false));

        groupTitle = (TextView) itemView.findViewById(R.id.group_name);
        groupList = (RecyclerView) itemView.findViewById(R.id.group_list);
    }

    public void bindGroup(final BaseRecipeGroup group,
                          RecipeSingleInteraction singleInteraction) {
        groupTitle.setText(group.getName());

        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(groupList.getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setInitialPrefetchItemCount(group.getRecipes().size());
        groupList.setLayoutManager(layoutManager);
        }

        if (adapter == null) {
            adapter = new RecipeSingleCardListAdapter(singleInteraction, group.getRecipes());
            groupList.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        if (animator == null) {
            animator = new FlipCardListAnimator();
            groupList.setItemAnimator(animator);
        }

/*        groupList.clearOnScrollListeners();
            groupList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState != SCROLL_STATE_SETTLING) return;
                    int lastVisibleRecipe = ((LinearLayoutManager) recyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();
                   // groupInteraction.onRecipesScroll(getAdapterPosition(), lastVisibleRecipe);
                }
            });
        }*/
    }
}
