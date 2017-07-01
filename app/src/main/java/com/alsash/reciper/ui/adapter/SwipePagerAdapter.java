package com.alsash.reciper.ui.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.ui.view.SwipeViewPager;

/**
 * SwipePagerAdapter that represents tabs model from presenter
 */
public class SwipePagerAdapter extends FragmentStatePagerAdapter
        implements SwipeViewPager.OnPageSelectListener {

    private SwipeTab[] tabs;
    private Fragment[] fragments;
    private int position = POSITION_UNCHANGED;

    public SwipePagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public SwipePagerAdapter(FragmentManager fm, SwipeTab[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    public void setTabs(SwipeTab[] tabs) {
        this.tabs = tabs;
        this.fragments = null;
        position = POSITION_NONE;
        notifyDataSetChanged();
        this.position = POSITION_UNCHANGED;
    }

    public void setFragments(Fragment[] fragments) {
        tabs = null;
        this.fragments = fragments;
        position = POSITION_NONE;
        notifyDataSetChanged();
        this.position = POSITION_UNCHANGED;
    }

    @Override
    public boolean isSwipeEnabled(int position) {
        if (tabs == null) return false;
        return tabs[position].isSwiped();
    }

    @Override
    public Fragment getItem(int position) {
        if (tabs == null) return fragments[position];
        return tabs[position].getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return position;
    }

    @Override
    public int getCount() {
        if (tabs == null) return fragments.length;
        return tabs.length;
    }

    @Nullable
    @StringRes
    public Integer getPageTitleRes(int position) {
        if (tabs == null) return null;
        return tabs[position].getTitle();
    }

    @Nullable
    @DrawableRes
    public Integer getPageIconRes(int position) {
        if (tabs == null) return null;
        return tabs[position].getIcon();
    }

    public boolean hasSwipeBehaviour() {
        return tabs != null;
    }
}
