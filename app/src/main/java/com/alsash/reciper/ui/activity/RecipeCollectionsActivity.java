package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
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
    @Inject
    NavigationLogic navigator;

    private BottomNavigationView navigation;
    private SwipePagerAdapter adapter;
    private SwipeViewPager pager;

    @Override
    public void setCollections(Fragment[] collections) {
        if (adapter != null) return;
        adapter = new SwipePagerAdapter(getSupportFragmentManager(), collections);
    }

    @Override
    public void showCollection(int position) {
        if (navigation == null) return;
        Integer id = convertPositionToId(position);
        if (id == null) return;
        navigation.setSelectedItemId(id);
    }

    @Override
    public int shownCollection() {
        Integer position = convertIdToPosition(navigation.getSelectedItemId());
        if (position != null) return position;
        return 0;
    }

    @Override
    protected BasePresenter<RecipeCollectionsView> inject() {
        ((ReciperApp) getApplicationContext())
                .getUiRecipeCollectionsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setCollections(
                navigator.getFragmentCollections(getIntent()));
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_recipe_collections_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_recipe_collections_appbar_add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_collections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_recipe_collections_toolbar);
        setSupportActionBar(toolbar);
        setupDrawer(toolbar); // Call to parent BaseDrawerActivity
        pager = ((SwipeViewPager) findViewById(R.id.activity_recipe_collections_pager));
        pager.setAdapter(adapter);
        navigation = (BottomNavigationView) findViewById(R.id.activity_recipe_collections_nav);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Integer position = convertIdToPosition(item.getItemId());
                        if (position == null) return false;
                        pager.setCurrentItem(position, true);
                        return true;
                    }
                });
    }

    @IdRes
    @Nullable
    private Integer convertPositionToId(int position) {
        switch (position) {
            case 0:
                return R.id.activity_recipe_collections_nav_categories;
            case 1:
                return R.id.activity_recipe_collections_nav_grid;
            case 2:
                return R.id.activity_recipe_collections_nav_labels;
            default:
                return null;
        }
    }

    @Nullable
    private Integer convertIdToPosition(@IdRes int id) {
        switch (id) {
            case R.id.activity_recipe_collections_nav_categories:
                return 0;
            case R.id.activity_recipe_collections_nav_grid:
                return 1;
            case R.id.activity_recipe_collections_nav_labels:
                return 2;
            default:
                return null;
        }
    }
}
