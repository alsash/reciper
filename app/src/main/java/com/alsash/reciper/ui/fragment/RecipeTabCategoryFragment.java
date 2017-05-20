package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabCategoryPresenter;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;
import com.alsash.reciper.ui.adapter.RecipeGroupCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeCardListInteraction;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class RecipeTabCategoryFragment extends BaseFragment<RecipeTabCategoryView>
        implements RecipeTabCategoryView, RecipeCardListInteraction {

    @Inject
    RecipeTabCategoryPresenter presenter;
    @Inject
    AppNavigator navigator;

    private SwipeRefreshLayout refreshIndicator;
    private RecyclerView list;
    private RecipeGroupCardListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean needPagination;
    private RecyclerView.OnScrollListener paginationListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (!needPagination || newState != SCROLL_STATE_SETTLING) return;
            presenter.nextPagination(layoutManager.findLastVisibleItemPosition());
        }
    };

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
    public void onExpand(Recipe recipe) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe.getId());
        bottomDialog.show(getActivity().getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.toRecipeView(recipe.getId());
    }

    @Override
    public void setContainer(List<Category> categories) {
        adapter = new RecipeGroupCardListAdapter(categories, this);
    }

    @Override
    public void setPagination(boolean needPagination) {
        this.needPagination = needPagination;
    }

    @Override
    public void showLoading(boolean loading) {
        refreshIndicator.setEnabled(loading);
        refreshIndicator.setRefreshing(loading);
    }

    @Override
    public void showInsert(int insertPosition, int insertCount) {
        adapter.notifyItemRangeInserted(insertPosition, insertCount);
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
        layoutManager = new LinearLayoutManager(list.getContext());
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        list.addOnScrollListener(paginationListener);
    }

    private void setupRefresh() {
        refreshIndicator.setColorSchemeResources(
                R.color.nutrition_carbohydrate,
                R.color.nutrition_fat,
                R.color.nutrition_protein);
    }
}
