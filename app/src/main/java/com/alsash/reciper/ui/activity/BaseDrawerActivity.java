package com.alsash.reciper.ui.activity;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alsash.reciper.R;

public abstract class BaseDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ViewGroup drawerContent;
    private NavigationView drawerNav;
    private View drawerNavHeader;

    @IdRes
    @Nullable
    protected abstract Integer getNavItemId();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_drawer_base);
        bindViews();
        LayoutInflater.from(this).inflate(layoutResID, drawerContent, true);
    }

    private void bindViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_drawer_base_layout);
        drawerContent = (ViewGroup) findViewById(R.id.activity_drawer_base_view_group);
        drawerNav = (NavigationView) findViewById(R.id.activity_drawer_base_nav);
        drawerNavHeader = drawerNav.getHeaderView(0);
    }

    /**
     * Setup Navigation Drawer after toolbar have been bound
     * @param toolbar child activity's toolbar.
     *                Null if homeAsUpButton is enabled
     */
    protected final void setupDrawer(@Nullable Toolbar toolbar) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(toolbar != null);
        drawerToggle.syncState();

        drawerNav.setNavigationItemSelectedListener(this);
        if (getNavItemId() != null) {
            drawerNav.setCheckedItem(getNavItemId());
        }
        final ImageButton button = (ImageButton) drawerNavHeader.findViewById(R.id.drawer_account_details);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int headers = drawerNav.getHeaderCount();
                if (headers == 1) {
                    button.animate().rotation(180F).start();
                    drawerNav.inflateHeaderView(R.layout.drawer_base_statistic);
                } else {
                    button.animate().rotation(0F).start();
                    drawerNav.removeHeaderView(drawerNav.getHeaderView(1));
                }
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.drawer_recipe_list:
                break;
            case R.id.drawer_food:
                break;
            case R.id.drawer_cart:
                break;
            case R.id.drawer_label:
                break;
            case R.id.drawer_category:
                break;
            case R.id.drawer_settings:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
