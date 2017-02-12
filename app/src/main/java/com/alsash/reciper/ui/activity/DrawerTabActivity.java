package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.alsash.reciper.R;

public abstract class DrawerTabActivity extends DrawerBaseActivity {

    protected abstract PagerAdapter getPagerAdapter(FragmentManager fm);

    @Override
    protected int getDrawerLayout() {
        return R.layout.activity_drawer_tab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the ViewPager with the implemented PagerAdapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.tab_container);
        viewPager.setAdapter(getPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
