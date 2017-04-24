package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;
import com.alsash.reciper.mvp.view.RecipeTabView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;

import java.util.List;

public class RecipeTabActivity extends BaseSwipeTabActivity implements RecipeTabView {

    private RecipeTabPresenter presenter;
    private SwipePagerAdapter adapter;
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

    // Interfaces implementations
    @Override
    public void setDrawTabTitleOnHeader(boolean drawTabTitleOnHeader) {
        this.drawTabTitleOnHeader = drawTabTitleOnHeader;
    }

    @Override
    public void setTabs(List<SwipeTab> tabs) {
        adapter = new SwipePagerAdapter(getSupportFragmentManager(), tabs);
    }

    @Override
    public void showTab(int position) {
        pager.setCurrentItem(position);
    }

    // This implementations
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new RecipeTabPresenter();
        presenter.setView(this); // Attach view
        presenter.initView(); // This initialization - call to setters on the view interface
        super.onCreate(savedInstanceState); // Patent initialization - views binding
        presenter.completeView(); // Enable to use parent views
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.setView(null);
    }
}
