package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeListPresenter;
import com.alsash.reciper.mvp.view.RecipeListView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;
import com.alsash.reciper.ui.view.RecyclerViewHelper;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * An abstract view holder that shows list of Recipes with help of their Presenter
 */
public abstract class BaseRecipeGroupHolder<G extends BaseEntity> extends RecyclerView.ViewHolder
        implements RecipeListView {

    private RecipeListInteraction interaction;
    private BaseRecipeListPresenter<RecipeListView> presenter;
    private RecyclerView groupList;
    private RecipeCardListAdapter adapter;
    private boolean viewVisible;
    private boolean needPagination;
    private RecyclerView.OnScrollListener paginationListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView rv, int newState) {
            if (!needPagination || newState != SCROLL_STATE_SETTLING) return;
            int lastPosition = RecyclerViewHelper.getLastVisibleItemPosition(rv.getLayoutManager());
            presenter.nextPagination(lastPosition);
        }
    };

    public BaseRecipeGroupHolder(ViewGroup parent,
                                 @LayoutRes int layoutId,
                                 @IdRes int listId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        this.groupList = (RecyclerView) itemView.findViewById(listId);
    }

    public abstract void bindGroup(G group);

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. openListener
     */
    public abstract void setListeners(View.OnClickListener... listeners);

    @LayoutRes
    protected abstract int getRecipeCardLayout();

    @Override
    public abstract void showLoading(boolean loading);

    @Override
    public void showUpdate(int position) {
        if (adapter == null) return;
        adapter.notifyItemChanged(position);
    }

    public void setInteraction(RecipeListInteraction interaction) {
        this.interaction = interaction;
    }

    public void setPresenter(BaseRecipeListPresenter<RecipeListView> newPresenter) {
        showLoading(false);
        if (presenter == null) {
            presenter = newPresenter;
            presenter.attach(this);
            return;
        }
        if (!presenter.equals(newPresenter)) {
            presenter.detach();
            presenter = newPresenter;
            presenter.attach(this);
        }
    }

    @Override
    public boolean isViewVisible() {
        return viewVisible;
    }

    public void setViewVisible(boolean viewVisible) {
        this.viewVisible = viewVisible;
        if (presenter == null) return;
        if (viewVisible) {
            presenter.visible(this);
        } else {
            presenter.invisible(this);
        }
    }

    @Override
    public void setContainer(List<Recipe> recipes) {
        adapter = new RecipeCardListAdapter(interaction, recipes, getRecipeCardLayout());
        LinearLayoutManager layoutManager = new LinearLayoutManager(groupList.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        if (recipes.size() > layoutManager.getInitialPrefetchItemCount()) {
            layoutManager.setInitialPrefetchItemCount(recipes.size());
        }
        groupList.setLayoutManager(layoutManager);
        groupList.setNestedScrollingEnabled(false);
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
