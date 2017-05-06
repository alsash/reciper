package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.SwipeTab;

/**
 * A Tab View that can turn off paging by swipe
 */
public interface SwipeTabView extends TabView<SwipeTab> {
    void setDrawTabTitleOnHeader(boolean drawTabTitleOnHeader);
}
