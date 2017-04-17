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

    protected final List<SwipeTab> tabs;

    public SwipePagerAdapter(FragmentManager fm, List<SwipeTab> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public boolean isSwipeEnabled(int position) {
        return !tabs.get(position).hasNestedViews();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    public String getPageTitle(int position, Resources res) {
        Integer resId = tabs.get(position).getTitle();
        if (resId == null) return null;
        return res.getString(resId);
    }

    @Nullable
    public Drawable getPageIcon(int position, Resources res, @Nullable Resources.Theme theme) {
        Integer resId = tabs.get(position).getIcon();
        if (resId == null) return null;
        return VectorDrawableCompat.create(res, resId, theme);
    }
}
