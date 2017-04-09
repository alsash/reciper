package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.tab.SwipeTab;

public interface RecipeTabView extends BaseTabView<SwipeTab> {

    void setDrawTabTitleOnHeader(boolean drawTabTitleOnHeader);
}
