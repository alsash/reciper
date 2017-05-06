package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.SwipeTab;

import java.util.List;

/**
 * A Tab View that draw tabs by model and can turn off paging by swipe
 */
public interface SwipeTabView extends BaseView {

    void setDrawTabTitleOnHeader(boolean drawTabTitleOnHeader);

    void setTabs(List<? extends SwipeTab> tabs);

    void showTab(int position);

    int shownTab();
}
