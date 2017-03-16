package com.alsash.reciper.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.adapter.RecipeCatPagerAdapter;
import com.alsash.reciper.view.fragment.dialog.RecipeBottomDialog;
import com.alsash.reciper.view.xmlview.SwipeViewPager;

/**
 * The Activity that represents two tabs with RecipeCatPagerAdapter:
 * Tab one - RecipeLabelsFragment
 * Tab two - RecipeBookmarksFragment
 */
public class RecipeTabFavActivity extends BaseDrawerActivity implements RecipeListInteraction {

    // Adapters
    private RecipeCatPagerAdapter pagerAdapter;

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
        RecipeDetailActivity.start(RecipeTabFavActivity.this, recipe.getId());
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.drawer_recipe_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        bindViews();
        setupToolbar();
        setupPager();
        setupTabs();
        setupFab();
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_tab_toolbar);
        pager = (SwipeViewPager) findViewById(R.id.activity_tab_svp);
        tabs = (TabLayout) findViewById(R.id.activity_tab_tabs);
        fab = (FloatingActionButton) findViewById(R.id.activity_tab_fab);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setupDrawer(toolbar); // Call to parent BaseDrawerActivity
    }

    private void setupPager() {
        pagerAdapter = new RecipeCatPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(pagerAdapter.getPageTitle(position));
            }
        });
        setToolbarTitle(pagerAdapter.getPageTitle(0));
    }

    private void setupTabs() {
        tabs.setupWithViewPager(pager);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            assert tab != null;
            tab.setText(null); // Icons only
            tab.setIcon(pagerAdapter.getPageIcon(i));
        }
    }

    private void setToolbarTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(title);
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
