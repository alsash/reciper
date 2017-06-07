package com.alsash.reciper.mvp.model.tab;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A simple Tab model for representing tabs with SwipeViewPager
 */
public abstract class SwipeTab {
    private final Integer title;
    private final Integer icon;
    private final boolean isSwiped;

    public SwipeTab(Integer title, Integer icon, boolean isSwiped) {
        this.title = title;
        this.icon = icon;
        this.isSwiped = isSwiped;
    }

    public abstract Fragment getFragment();

    @StringRes
    public Integer getTitle() {
        return title;
    }

    @DrawableRes
    public Integer getIcon() {
        return icon;
    }

    public boolean isSwiped() {
        return isSwiped;
    }
}
