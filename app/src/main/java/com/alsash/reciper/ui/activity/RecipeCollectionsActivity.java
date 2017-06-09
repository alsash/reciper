package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionsPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionsView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.alsash.reciper.ui.view.SwipeViewPager;

import javax.inject.Inject;

public class RecipeCollectionsActivity extends BaseDrawerActivity<RecipeCollectionsView>
        implements RecipeCollectionsView {

    @Inject
    RecipeCollectionsPresenter presenter;
    private SwipeViewPager pager;
    private SwipePagerAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.activity_recipe_collections_nav_categories:
                    pager.setCurrentItem(0);
                    return true;
                case R.id.activity_recipe_collections_nav_grid:
                    pager.setCurrentItem(1);
                    return true;
                case R.id.activity_recipe_collections_nav_labels:
                    pager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void setCollections(Fragment[] collections) {
        if (adapter != null) return;
        adapter = new SwipePagerAdapter(getSupportFragmentManager(), collections);
        pager.setAdapter(adapter);
    }

    @Override
    public void showCollection(int position) {
        if (pager.getCurrentItem() != position) pager.setCurrentItem(position);
    }

    @Override
    protected BasePresenter<RecipeCollectionsView> inject() {
        ((ReciperApp) getApplicationContext())
                .getUiRecipeCollectionsComponent()
                .inject(this);
        return presenter; // BasePresenter will be embedded in the activity lifecycle
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_collections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_recipe_collections_toolbar);
        setSupportActionBar(toolbar);
        setupDrawer(toolbar); // Call to parent BaseDrawerActivity
        pager = ((SwipeViewPager) findViewById(R.id.activity_recipe_collections_pager));
        ((BottomNavigationView) findViewById(R.id.activity_recipe_collections_nav))
                .setOnNavigationItemSelectedListener(navigationListener);
    }
}
