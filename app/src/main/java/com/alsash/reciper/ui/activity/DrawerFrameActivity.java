package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;

public abstract class DrawerFrameActivity extends DrawerBaseActivity {

    protected abstract Fragment getFrameFragment(@Nullable Intent activityIntent);

    @Override
    protected int getDrawerLayout() {
        return R.layout.activity_drawer_frame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, getFrameFragment(getIntent()))
                .commit();
    }
}
