package com.alsash.reciper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

public class RecipeListActivity extends DrawerFrameActivity {

    @Override
    protected Fragment getFrameFragment(@Nullable Intent activityIntent) {
        return RecipeListFragment.newInstance();
    }

    @Override
    protected void setupFab(FloatingActionButton fab) {
        // fab.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
