package com.alsash.reciper.ui.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.ui.view.SwipeViewPager;

import java.util.List;

/**
 * SwipePagerAdapter that represents tabs model from presenter
 */
public class SwipePagerAdapter extends FragmentPagerAdapter
        implements SwipeViewPager.OnPageSelectListener {

    private List<? extends SwipeTab> tabs;
    private Fragment[] fragments;

    public SwipePagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public SwipePagerAdapter(FragmentManager fm, List<? extends SwipeTab> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public boolean isSwipeEnabled(int position) {
        if (tabs == null) return false;
        return tabs.get(position).isSwiped();
    }

    @Override
    public Fragment getItem(int position) {
        if (tabs == null) return fragments[position];
        return tabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        if (tabs == null) return fragments.length;
        return tabs.size();
    }

    @Nullable
    public String getPageTitle(int position, Resources res) {
        if (tabs == null) return null;
        Integer resId = tabs.get(position).getTitle();
        return (resId == null) ? null : res.getString(resId);
    }

    @Nullable
    public Drawable getPageIcon(int position, Resources res, @Nullable Resources.Theme theme) {
        if (tabs == null) return null;
        Integer resId = tabs.get(position).getIcon();
        return (resId == null) ? null : VectorDrawableCompat.create(res, resId, theme);
    }

    public boolean hasSwipeBehaviour() {
        return tabs != null;
    }
}
