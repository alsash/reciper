package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;
import com.alsash.reciper.mvp.view.RecipeTabView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.hannesdorfmann.mosby3.mvp.delegate.ActivityMvpDelegateImpl;
import com.hannesdorfmann.mosby3.mvp.delegate.MvpDelegateCallback;

import java.util.List;

public class RecipeTabActivity extends BaseSwipeTabActivity {

    // Mvp
    private RecipeTabView mvpView;
    private RecipeTabPresenter mvpPresenter;
    private MvpDelegateCallback<RecipeTabView, RecipeTabPresenter> mvpCallback;
    private ActivityMvpDelegateImpl<RecipeTabView, RecipeTabPresenter> mvpDelegate;

    private SwipePagerAdapter adapter;
    private List<SwipeTab> tabs;
    private boolean drawTabTitleOnHeader;

    // Super implementations
    @Override
    protected SwipePagerAdapter getPagerAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return null;
    }

    @Override
    protected void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected boolean isDrawTabTitleOnHeader() {
        return drawTabTitleOnHeader;
    }

    // This implementations
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupMvpView();
        setupMvpPresenter();
        setupMvpCallback();
        setupMvpDelegate();
        mvpDelegate.onCreate(savedInstanceState);
        setupAdapter();
        super.onCreate(savedInstanceState);
    }

    protected void setupAdapter() {
        mvpPresenter.loadTabs();
        adapter = new SwipePagerAdapter(getSupportFragmentManager(), tabs);
        mvpPresenter.showTabs();
    }

    void setupMvpView() {
        mvpView = new RecipeTabView() {
            @Override
            public void setDrawTabTitleOnHeader(boolean draw) {
                drawTabTitleOnHeader = draw;
            }

            @Override
            public void setTabs(List<SwipeTab> newTabs) {
                tabs = newTabs;
            }

            @Override
            public void showTab(int position, boolean smooth) {
                pager.setCurrentItem(position, smooth);
            }
        };
    }

    void setupMvpPresenter() {
        mvpPresenter = new RecipeTabPresenter();
    }

    void setupMvpCallback() {
        mvpCallback = new MvpDelegateCallback<RecipeTabView, RecipeTabPresenter>() {
            @NonNull
            @Override
            public RecipeTabPresenter createPresenter() {
                return mvpPresenter;
            }

            @Override
            public RecipeTabPresenter getPresenter() {
                return mvpPresenter;
            }

            @Override
            public void setPresenter(RecipeTabPresenter newPresenter) {
                mvpPresenter = newPresenter;
            }

            @Override
            public RecipeTabView getMvpView() {
                return mvpView;
            }
        };
    }

    void setupMvpDelegate() {
        mvpDelegate = new ActivityMvpDelegateImpl<>(this, mvpCallback, false);
    }

    // This delegations
    @Override
    public void onDestroy() {
        super.onDestroy();
        mvpDelegate.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvpDelegate.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpDelegate.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvpDelegate.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvpDelegate.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mvpDelegate.onRestart();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mvpDelegate.onContentChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mvpDelegate.onPostCreate(savedInstanceState);
    }

}
