package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.BaseTab;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

/**
 * A simple BaseTabView for BaseSwipeTabActivity
 */
public interface BaseTabView<T extends BaseTab> extends MvpView {

    void setTabs(List<T> tabs);

    void showTab(int position, boolean smooth);
}
