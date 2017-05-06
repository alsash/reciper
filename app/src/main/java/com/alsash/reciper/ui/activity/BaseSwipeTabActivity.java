package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.SwipeTabView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.alsash.reciper.ui.view.SwipeViewPager;

import java.util.List;

/**
 * An abstract BaseActivity that holds tab layout
 */
public abstract class BaseSwipeTabActivity<V extends SwipeTabView> extends BaseDrawerActivity<V>
        implements SwipeTabView {

    // Layout views
    protected Toolbar toolbar;
    protected SwipeViewPager pager;
    protected TabLayout tabs;
    protected FloatingActionButton fab;

    private SwipePagerAdapter adapter;
    private boolean drawTabTitleOnHeader;

    protected abstract void setupFab();

    @Override
    public void setDrawTabTitleOnHeader(boolean drawTabTitleOnHeader) {
        this.drawTabTitleOnHeader = drawTabTitleOnHeader;
    }

    @Override
    public void setTabs(List<SwipeTab> tabs) {
        adapter = getPagerAdapter(tabs);
    }

    @Override
    public void showTab(int position) {
        if (pager.getCurrentItem() != position) {
            pager.setCurrentItem(position); // Title set by listener
        }
    }

    @Override
    public int shownTab() {
        return pager.getCurrentItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_swipe_base);
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

    protected void setupPager() {
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        if (drawTabTitleOnHeader) {
            pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    setToolbarTitle(position);
                }
            });
            setToolbarTitle(pager.getCurrentItem());
        }
    }

    protected void setupTabs() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            assert tab != null;
            if (drawTabTitleOnHeader) {
                tab.setText(null);
            } else {
                tab.setText(adapter.getPageTitle(i, getResources()));
            }
            tab.setIcon(adapter.getPageIcon(i, getResources(), getTheme()));
        }
    }

    protected SwipePagerAdapter getPagerAdapter(List<SwipeTab> tabs) {
        return new SwipePagerAdapter(getSupportFragmentManager(), tabs);
    }

    protected void setToolbarTitle(int position) {
        String title = adapter.getPageTitle(position, getResources());
        if (title == null) return;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(title);
    }
}
