package com.alsash.reciper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.vector.VectorHelper;

public abstract class DrawerSpinnerActivity extends DrawerBaseActivity {

    @Nullable
    protected abstract Fragment getSpinnerFragment(int menuItemId, @Nullable Intent activityIntent);

    @MenuRes
    protected abstract int getSpinnerMenuRes();

    @IdRes
    protected abstract int getSpinnerMenuItemDefault();

    @Override
    protected int getDrawerLayout() {
        return R.layout.activity_drawer_spinner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.spinner_container,
                        getSpinnerFragment(getSpinnerMenuItemDefault(), getIntent()))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.spinner, menu);
        SubMenu subMenu = menu.getItem(0).getSubMenu();
        subMenu.clear();
        inflater.inflate(getSpinnerMenuRes(), subMenu);

        new VectorHelper(this).tintMenuItems(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getSpinnerFragment(item.getItemId(), getIntent());
        if (fragment != null) {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            if (item.getItemId() == getSpinnerMenuItemDefault()) {
                actionBar.setSubtitle(null);
            } else {
                actionBar.setSubtitle(item.getTitle());
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.spinner_container, fragment)
                    .commit();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
