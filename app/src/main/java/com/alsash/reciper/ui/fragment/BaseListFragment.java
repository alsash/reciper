package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.presenter.BaseListPresenter;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.ui.view.RecyclerViewHelper;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Base injection-ready fragment, that shows list of Entities
 *
 * @param <M> - Model of an entity
 * @param <V> - View interface
 */
public abstract class BaseListFragment<M extends BaseEntity, V extends BaseListView<M>>
        extends BaseFragment<V>
        implements BaseListView<M> {

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
        presenter = (BaseListPresenter<M, V>) getThisPresenter();
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
        refreshIndicator.setRefreshing(loading);
    }

    @Override
    public void showInsert(int insertPosition, int insertCount) {
        adapter.notifyItemRangeInserted(insertPosition, insertCount);
    }

    @Override
    public void showUpdate(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_list_refresh, container, false);
        bindViews(layout);
        setupList();
        setupRefresh();
        return layout;
    }

    protected void bindViews(View layout) {
        refreshIndicator = (SwipeRefreshLayout) layout.findViewById(R.id.list_refresh_indicator);
        list = (RecyclerView) layout.findViewById(R.id.list_refresh_rv);
    }

    protected void setupList() {
        list.setLayoutManager(getLayoutManager(list.getContext()));
        list.setAdapter(adapter);
        list.addOnScrollListener(paginationListener);
    }

    protected void setupRefresh() {
        refreshIndicator.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh(getThisView());
            }
        });
        refreshIndicator.setColorSchemeResources(
                R.color.nutrition_carbohydrate,
                R.color.nutrition_fat,
                R.color.nutrition_protein);
    }

    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
