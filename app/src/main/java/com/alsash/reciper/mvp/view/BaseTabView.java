package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.BaseTab;

import java.util.List;

/**
 * A simple BaseTabView
 */
public interface BaseTabView<T extends BaseTab> {

    void setTabs(List<T> tabs);

    void showTab(int position);

    int shownTab();
}
