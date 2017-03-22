package com.alsash.reciper.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.alsash.reciper.R;
import com.alsash.reciper.view.adapter.SwipePagerAdapter;
import com.alsash.reciper.view.xmlview.SwipeViewPager;

/**
 * An abstract Activity that holds tab layout
 */
public abstract class BaseTabActivity extends BaseDrawerActivity {

    // Views
    protected Toolbar toolbar;
    protected SwipeViewPager pager;
    protected TabLayout tabs;
    protected FloatingActionButton fab;

    protected abstract SwipePagerAdapter getPagerAdapter();

    protected abstract void setupFab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_base);
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
        if (drawTabTitle()) toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        setupDrawer(toolbar); // Call to parent BaseDrawerActivity
    }

    private void setupPager() {
        pager.setAdapter(getPagerAdapter());
        if (!drawTabTitle()) {
            pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    setToolbarTitle(getPagerAdapter().getPageTitle(position));
                }
            });
            setToolbarTitle(getPagerAdapter().getPageTitle(0));
        }
        tabs.setupWithViewPager(pager);
    }

    private void setupTabs() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            assert tab != null;
            if (!drawTabTitle()) tab.setText(null); // Icons only
            if (drawTabIcons()) tab.setIcon(getPagerAdapter().getPageIcon(i));
        }
    }

    protected void setToolbarTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(title);
    }

    protected boolean drawTabIcons() {
        return false;
    }

    protected boolean drawTabTitle() {
        return true;
    }
}
