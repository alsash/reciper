package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabCategoryPresenter;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class RecipeTabCategoryFragment extends BaseFragment<RecipeTabCategoryView>
        implements RecipeTabCategoryView {

    @Inject
    RecipeTabCategoryPresenter presenter;

    private SwipeRefreshLayout refreshIndicator;
    private RecyclerView list;
    private RecipeGroupCardListAdapter adapter;
    private RecyclerView.OnScrollListener paginationListener;

    public static RecipeTabCategoryFragment newInstance() {
        return new RecipeTabCategoryFragment();
    }

    @Override
    protected BasePresenter<RecipeTabCategoryView> inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getRecipeTabComponent()
                .inject(this);
        return presenter; // BasePresenter will be embedded in fragment lifecycle
    }

    @Override
    public void setContainer(List<Category> container) {
        adapter = new RecipeGroupCardListAdapter(container, new RecipeListInteraction() {
            @Override
            public void onExpand(Recipe recipe) {
                presenter.onRecipeExpand(getActivity().getSupportFragmentManager(), recipe);
            }

            @Override
            public void onOpen(Recipe recipe) {
                presenter.onRecipeOpen(getActivity().getApplicationContext(), recipe);
            }
        });
    }

    @Override
    public void startPagination() {
        if (paginationListener != null) return;
        paginationListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != SCROLL_STATE_SETTLING) return;
                int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                presenter.onPagination(lastVisiblePosition);
            }
        };
    }

    @Override
    public void stopPagination() {
        list.removeOnScrollListener(paginationListener);
    }

    @Override
    public void showLoading(boolean loading) {
        refreshIndicator.setEnabled(loading);
        refreshIndicator.setRefreshing(loading);
    }

    @Override
    public void showInsert(int insertPosition) {
        adapter.notifyItemInserted(insertPosition);
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

    private void bindViews(View layout) {
        refreshIndicator = (SwipeRefreshLayout) layout.findViewById(R.id.list_refresh_indicator);
        list = (RecyclerView) layout.findViewById(R.id.list_refresh_rv);
    }

    private void setupList() {
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        // An Adapter has been created at setCategories()
        list.setAdapter(adapter);
        // A PaginationListener has been created at startPagination()
        if (paginationListener != null) list.addOnScrollListener(paginationListener);
    }

    private void setupRefresh() {
        refreshIndicator.setColorSchemeResources(
                R.color.nutrition_carbohydrate,
                R.color.nutrition_fat,
                R.color.nutrition_protein);
        showLoading(false);
    }
}
