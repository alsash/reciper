package com.alsash.reciper.ui.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.presenter.BaseListPresenter;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.ui.view.helper.RecyclerViewHelper;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Base injection-ready fragment, that shows list of Entities
 *
 * @param <M> - Model of an entity
 * @param <V> - View interface
 */
public abstract class BaseDialogListFragment<M extends BaseEntity, V extends BaseListView<M>>
        extends BaseDialogFragment<V> implements BaseListView<M> {

    protected BaseListPresenter<M, V> presenter;
    protected SwipeRefreshLayout refreshIndicator;
    protected RecyclerView list;
    protected RecyclerView.Adapter adapter;
    protected boolean needPagination;
    protected RecyclerView.OnScrollListener paginationListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView rv, int newState) {
            if (!needPagination || newState != SCROLL_STATE_SETTLING) return;
            int position = RecyclerViewHelper.getLastVisibleItemPosition(rv.getLayoutManager());
            presenter.nextPagination(position);
        }
    };

    @Override
    protected abstract BaseListPresenter<M, V> inject();

    protected abstract RecyclerView.Adapter getAdapter(List<M> container);

    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = getThisPresenter();
    }

    @Override
    public void setContainer(List<M> container) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = getAdapter(container);
        }
    }

    @Override
    public void setPagination(boolean needPagination) {
        this.needPagination = needPagination;
    }

    @Override
    public void showLoading(boolean loading) {
        if (refreshIndicator != null) refreshIndicator.setRefreshing(loading);
    }

    @Override
    public void showInsert(int position) {
        if (adapter != null) adapter.notifyItemInserted(position);
        if (list != null) list.scrollToPosition(position);
    }

    @Override
    public void showInsert(int insertPosition, int insertCount) {
        if (adapter != null) adapter.notifyItemRangeInserted(insertPosition, insertCount);
    }

    @Override
    public void showUpdate(int position) {
        if (adapter != null) adapter.notifyItemChanged(position);
    }

    @Override
    public void showUpdate() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public void showDelete(int position) {
        if (adapter != null) adapter.notifyItemRemoved(position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (list != null) {
            list.setAdapter(adapter);
            list.addOnScrollListener(paginationListener);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
