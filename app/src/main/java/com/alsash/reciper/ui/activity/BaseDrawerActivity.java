package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IdRes;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alsash.reciper.R;

/**
 * An abstract activity that implements main Navigation Drawer
 */
public abstract class BaseDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int NAV_STARTER_POST_DELAY_MS = 150;

    private DrawerLayout drawerLayout;
    private ViewGroup drawerContent;
    private NavigationView drawerNav;
    private View drawerNavHeader;

    private Handler navHandler = new Handler();
    private Runnable navStarter = null;

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
     *
     * @param toolbar child activity's toolbar.
     *                Null if homeAsUpButton is enabled
     */
    protected final void setupDrawer(@Nullable Toolbar toolbar) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(toolbar != null);
        drawerToggle.syncState();
        drawerNav.setNavigationItemSelectedListener(this);

        if (getNavItemId() != null) drawerNav.setCheckedItem(getNavItemId());

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
    protected void onPause() {
        super.onPause();
        if (navStarter != null) {
            navHandler.removeCallbacks(navStarter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navStarter != null) {
            navHandler.postDelayed(navStarter, NAV_STARTER_POST_DELAY_MS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // back stack for child activity's navigation
        if (getNavItemId() == null && item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int thisId = getNavItemId() != null ? getNavItemId() : 0;

        if (id != thisId) {
            Class<?> thisClass = getClass();
            Class<?> nextClass = null;
            switch (id) {
                case R.id.navigation_recipes:
                    nextClass = RecipeTabActivity.class;
                    break;
//                case R.id.drawer_base_nav_recipe_favorite:
//                    nextClass = RecipeTabFavActivity.class;
//                    break;
                case R.id.navigation_cart:
                    break;
                case R.id.drawer_label:
                    break;
                case R.id.drawer_category:
                    break;
                case R.id.drawer_settings:
                    break;
            }
            if (nextClass != null && !thisClass.equals(nextClass)) {
                postNavStarter(newNavStarter(new Intent(this, nextClass)));
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private Runnable newNavStarter(Intent starter) {
        return new Runnable() {
            boolean isRoot = getNavItemId() != null;
            private Intent starter;

            @Override
            public void run() {
                startActivity(starter);
                finish();
                overridePendingTransition(0, 0); // No animation on finish
                navStarter = null;
            }

            Runnable setStarter(Intent starter) {
                if (isRoot) {   // Same task
                    starter.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {        // New task
                    starter.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                this.starter = starter;
                return this;
            }
        }.setStarter(starter);
    }

    private void postNavStarter(Runnable newNavStarter) {
        if (navStarter != null) {
            navHandler.removeCallbacks(navStarter); // Run only last chosen item
        }
        navStarter = newNavStarter;
        navHandler.postDelayed(navStarter, NAV_STARTER_POST_DELAY_MS);
    }
}
