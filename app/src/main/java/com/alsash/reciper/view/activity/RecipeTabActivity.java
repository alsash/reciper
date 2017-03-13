package com.alsash.reciper.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.view.fragment.RecipeListFragment;
import com.alsash.reciper.view.views.MotionEventViewPager;

public class RecipeTabActivity extends BaseDrawerActivity {

    // Views
    private Toolbar toolbar;
    private MotionEventViewPager pager;
    private TabLayout tabs;
    private FloatingActionButton fab;

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
        pager = (MotionEventViewPager) findViewById(R.id.activity_recipe_tab_pager);
        tabs = (TabLayout) findViewById(R.id.activity_recipe_tab_tabs);
        fab = (FloatingActionButton) findViewById(R.id.activity_recipe_tab_fab);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setupDrawer(toolbar); // parent BaseDrawerActivity call
    }

    private void setupPager() {
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
    }

    private void setupTabs() {
        tabs.setupWithViewPager(pager);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return RecipeListFragment.newInstance();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
