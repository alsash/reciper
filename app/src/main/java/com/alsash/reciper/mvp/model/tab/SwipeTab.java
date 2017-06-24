package com.alsash.reciper.mvp.model.tab;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A simple Tab model for representing tabs with SwipeViewPager
 */
public class SwipeTab {

    private Class<? extends Fragment> fragmentClass;
    private Bundle fragmentArgs;
    private Integer title;
    private Integer icon;
    private boolean swiped;

    private SwipeTab() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Fragment getFragment() {
        try {
            Fragment fragment = fragmentClass.newInstance();
            fragment.setArguments(fragmentArgs);
            return fragment;
        } catch (Exception e) {
            return null;
        }
    }

    @StringRes
    public Integer getTitle() {
        return title;
    }

    @DrawableRes
    public Integer getIcon() {
        return icon;
    }

    public boolean isSwiped() {
        return swiped;
    }

    public static class Builder {
        private SwipeTab tab;

        private Builder() {
            tab = new SwipeTab();
        }

        public Builder fragment(Fragment fragment) {
            tab.fragmentClass = fragment.getClass();
            tab.fragmentArgs = fragment.getArguments();
            return this;
        }

        public Builder title(@StringRes Integer title) {
            tab.title = title;
            return this;
        }

        public Builder icon(@DrawableRes Integer icon) {
            tab.icon = icon;
            return this;
        }

        public Builder swiped(boolean swiped) {
            tab.swiped = swiped;
            return this;
        }

        public SwipeTab build() {
            SwipeTab tab = this.tab;
            this.tab = null;
            return tab;
        }
    }
}
