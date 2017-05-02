package com.alsash.reciper.mvp.model.tab;

/**
 * A simple holder for representing tabs in Activity with SwipeViewPager
 */
public abstract class SwipeTab extends BaseTab {

    private boolean isSwiped;

    public SwipeTab(Integer title, Integer icon, boolean isSwiped) {
        super(title, icon);
        this.isSwiped = isSwiped;
    }

    public boolean isSwiped() {
        return isSwiped;
    }
}
