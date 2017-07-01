package com.alsash.reciper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.view.BaseView;

/**
 * An abstract activity that implements main Navigation Drawer
 */
public abstract class BaseDrawerActivity<V extends BaseView> extends BaseActivity<V>
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView drawerNav;
    private DrawerToggle drawerToggle;

    protected abstract NavigationLogic getNavigationLogic();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_base_drawer_layout);
        drawerNav = (NavigationView) findViewById(R.id.activity_base_drawer_navigation);
        LayoutInflater.from(this).inflate(layoutResID,
                (ViewGroup) findViewById(R.id.activity_base_drawer_container), true);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        drawerToggle = new DrawerToggle(this, drawerLayout, toolbar);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerNav.setNavigationItemSelectedListener(this);
        setupDrawer();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setupDrawer();
    }

    private void setupDrawer() {
        Integer navItemId = getNavigationLogic().getNavigationItemId(getIntent());
        Integer navTitle = getNavigationLogic().getNavigationItemTitleRes(getIntent());
        drawerToggle.setDrawerIndicatorEnabled(navItemId != null);
        drawerToggle.syncState();
        if (navItemId != null)
            drawerNav.setCheckedItem(navItemId);
        if (getSupportActionBar() != null) {
            if (navTitle != null) getSupportActionBar().setTitle(navTitle);
            if (navItemId == null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // back stack for child activity's navigation
        Integer navItemId = getNavigationLogic().getNavigationItemId(getIntent());
        if (navItemId == null && item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Integer oldId = getNavigationLogic().getNavigationItemId(getIntent());
        final int newId = item.getItemId();
        if (oldId != null && !oldId.equals(newId))
            getNavigationLogic().fromActivity(getThisContext()).toNavigationItemId(newId);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class DrawerToggle extends ActionBarDrawerToggle {

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
            super(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        }

    }
}
