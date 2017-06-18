package com.alsash.reciper.mvp.model.tab;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

/**
 * A simple Tab model for representing tabs with SwipeViewPager
 */
public class SwipeTab {

    private static Builder builder = new Builder();

    private Fragment fragment;
    private Integer title;
    private Integer icon;
    private boolean swiped;

    private SwipeTab() {
    }

    public SwipeTab(Fragment fragment,
                    @StringRes Integer title,
                    @DrawableRes Integer icon,
                    boolean swiped) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
        this.swiped = swiped;
    }

    public static Builder builder() {
        return builder.refresh();
    }

    public Fragment getFragment() {
        return fragment;
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
        }

        private Builder refresh() {
            tab = new SwipeTab();
            return this;
        }

        public Builder fragment(Fragment fragment) {
            tab.fragment = fragment;
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
