package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.BaseTab;

import java.util.List;

/**
 * A Tab View that draw tabs by model
 */
public interface TabView<T extends BaseTab> extends BaseView {

    void setTabs(List<T> tabs);

    void showTab(int position);

    int shownTab();
}
