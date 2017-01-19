package com.alsash.reciper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;

public class RecipeListActivity extends DrawerSpinnerActivity {

    @Override
    @Nullable
    protected Fragment getSpinnerFragment(int menuItemId, @Nullable Intent activityIntent) {
        switch (menuItemId) {
            case R.id.group_all:
            case R.id.group_bookmark:
            case R.id.group_label:
            case R.id.group_category:
            case R.id.group_food:
                return RecipeListFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    protected int getSpinnerMenuRes() {
        return R.menu.recipe_group;
    }

    @Override
    protected int getSpinnerMenuItemDefault() {
        return R.id.group_all;
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
