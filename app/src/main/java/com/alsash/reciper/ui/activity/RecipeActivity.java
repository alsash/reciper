package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

public class RecipeActivity extends DrawerItemActivity {

    @Override
    protected Fragment getItemFragment(@Nullable Intent activityIntent) {
        return new Fragment();
    }

    @Override
    protected void setupFab(FloatingActionButton fab) {
        fab.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
