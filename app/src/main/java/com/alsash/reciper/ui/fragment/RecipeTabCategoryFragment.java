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

public class RecipeTabCategoryFragment extends BaseFragment<RecipeTabCategoryView>
        implements RecipeTabCategoryView {

    @Inject
    RecipeTabCategoryPresenter presenter;

    private SwipeRefreshLayout refreshIndicator;
    private RecyclerView list;
    private RecipeGroupCardListAdapter adapter;

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
    public void showLoading(boolean loading) {
        if (loading) {
            refreshIndicator.setEnabled(true);
            refreshIndicator.setRefreshing(true);
        } else {
            refreshIndicator.setRefreshing(false);
            refreshIndicator.setEnabled(false);
        }
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
        list.setAdapter(adapter); // Created at setCategories()
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int position = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                presenter.onScroll(position);
            }
        });
    }

    private void setupRefresh() {
        refreshIndicator.setColorSchemeResources(
                R.color.nutrition_carbohydrate,
                R.color.nutrition_fat,
                R.color.nutrition_protein);
        showLoading(false);
    }
}
