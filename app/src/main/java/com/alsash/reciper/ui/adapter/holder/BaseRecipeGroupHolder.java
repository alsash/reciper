package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseGroup;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupListPresenter;
import com.alsash.reciper.mvp.view.RecipeListView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * An abstract view holder that shows list of Recipes with help of their Presenter
 */
public abstract class BaseRecipeGroupHolder<G extends BaseGroup> extends RecyclerView.ViewHolder
        implements RecipeListView {

    protected BaseRecipeGroupListPresenter presenter;
    private RecipeListInteraction interaction;
    private RecyclerView groupList;
    private LinearLayoutManager layoutManager;
    private RecipeCardListAdapter adapter;
    private boolean viewVisible;
    private boolean needPagination;
    private RecyclerView.OnScrollListener paginationListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (!needPagination || newState != SCROLL_STATE_SETTLING) return;
            presenter.nextPagination(layoutManager.findLastVisibleItemPosition());
        }
    };

    public BaseRecipeGroupHolder(ViewGroup parent,
                                 @LayoutRes int layoutId,
                                 @IdRes int listId,
                                 BaseRecipeGroupListPresenter presenter,
                                 RecipeListInteraction interaction) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        this.groupList = (RecyclerView) itemView.findViewById(listId);
        this.interaction = interaction;
        this.presenter = presenter;
    }

    @CallSuper
    public void bindGroup(G group) {
        presenter.setGroupId(group.getId());
        presenter.refresh(this);
    }

    public void onRecycled() {
        presenter.detach();
    }

    @Override
    public boolean isViewVisible() {
        return viewVisible;
    }

    public void setViewVisible(boolean viewVisible) {
        this.viewVisible = viewVisible;
    }

    @Override
    public void setContainer(List<Recipe> recipes) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            return;
        }
        layoutManager = new LinearLayoutManager(groupList.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        groupList.setLayoutManager(layoutManager);
        adapter = new RecipeCardListAdapter(interaction, recipes);
        groupList.setAdapter(adapter);
        groupList.setItemAnimator(new FlipCardListAnimator());
        groupList.addOnScrollListener(paginationListener);
    }

    @Override
    public void setPagination(boolean needPagination) {
        this.needPagination = needPagination;
    }

    @Override
    public void showInsert(int insertPosition, int insertCount) {
        adapter.notifyItemRangeInserted(insertPosition, insertCount);
    }
}
