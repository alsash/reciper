package com.alsash.reciper.mvp.model.tab;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A simple holder for representing tabs in Activity
 */
public abstract class BaseTab {

    private final Integer title;
    private final Integer icon;

    public BaseTab(Integer title, Integer icon) {
        this.title = title;
        this.icon = icon;
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
}
