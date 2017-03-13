package com.alsash.reciper.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.adapter.SwipePagerAdapter;
import com.alsash.reciper.view.fragment.dialog.RecipeBottomDialog;
import com.alsash.reciper.view.views.SwipeViewPager;

public class RecipeTabActivity extends BaseDrawerActivity implements RecipeListInteraction {

    // Adapters
    private SwipePagerAdapter pagerAdapter;

    // Views
    private Toolbar toolbar;
    private SwipeViewPager pager;
    private TabLayout tabs;
    private FloatingActionButton fab;

    @Override
    public void onExpand(Recipe recipe, int position) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe);
        bottomDialog.show(getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void onOpen(Recipe recipe, int position) {
        RecipeDetailActivity.start(RecipeTabActivity.this, recipe.getId());
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.drawer_recipe_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_tab);
        bindViews();
        setupToolbar();
        setupPager();
        setupTabs();
        setupFab();
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_tab_toolbar);
        pager = (SwipeViewPager) findViewById(R.id.activity_recipe_tab_pager);
        tabs = (TabLayout) findViewById(R.id.activity_recipe_tab_tabs);
        fab = (FloatingActionButton) findViewById(R.id.activity_recipe_tab_fab);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setupDrawer(toolbar); // parent BaseDrawerActivity call
    }

    private void setupPager() {
        pagerAdapter = new SwipePagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pager.setSwipeEnabled(pagerAdapter.isSwipeEnabled(position));
                setupTabs();
            }
        });
        tabs.setupWithViewPager(pager);
    }

    private void setupTabs() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            assert tab != null;
            int tintColor = (pager.getCurrentItem() == i) ? R.color.white : R.color.white_a80;
            tab.setIcon(pagerAdapter.getPageIcon(i, tintColor));
        }
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
