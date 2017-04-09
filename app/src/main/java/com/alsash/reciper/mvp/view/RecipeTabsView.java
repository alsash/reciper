package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.SwipeTab;

public interface RecipeTabsView extends BaseTabsView<SwipeTab> {

    void setDrawTabTitleOnHeader(boolean drawTabTitleOnHeader);
}
