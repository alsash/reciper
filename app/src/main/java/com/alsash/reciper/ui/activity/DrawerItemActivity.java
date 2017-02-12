package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;

public abstract class DrawerItemActivity extends DrawerBaseActivity {

    protected abstract Fragment getItemFragment(@Nullable Intent activityIntent);

    @Override
    protected int getDrawerLayout() {
        return R.layout.activity_drawer_item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_container, getItemFragment(getIntent()))
                .commit();
    }
}
