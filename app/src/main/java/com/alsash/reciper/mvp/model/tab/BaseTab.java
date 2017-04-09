package com.alsash.reciper.mvp.model.tab;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A simple holder for representing tabs in Activity
 */
public interface BaseTab {

    Fragment getFragment();

    @StringRes
    Integer getTitle();

    @DrawableRes
    Integer getIcon();
}
