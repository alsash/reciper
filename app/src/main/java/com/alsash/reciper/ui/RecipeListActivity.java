package com.alsash.reciper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;

public class RecipeListActivity extends DrawerSpinnerActivity {

    private static final SpinnerArrayRes spinnerRes = new SpinnerArrayRes(
            R.array.spinner_recipe_group_names, null);

    @Override
    protected Fragment getSpinnerFragment(int position, @Nullable Intent activityIntent) {
        return RecipeListFragment.newInstance();
    }

    @Override
    protected SpinnerArrayRes getSpinnerArrayRes() {
        return spinnerRes;
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
