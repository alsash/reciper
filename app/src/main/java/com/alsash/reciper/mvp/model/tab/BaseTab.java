package com.alsash.reciper.mvp.model.tab;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A simple holder for representing tabs in Activity
 */
public interface BaseTab {

    @NonNull
    Fragment getFragment();

    @StringRes
    @Nullable
    Integer getTitle();

    @DrawableRes
    @Nullable
    Integer getIcon();
}
