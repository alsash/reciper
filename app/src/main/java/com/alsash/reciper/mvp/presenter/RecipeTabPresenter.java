package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.RecipeTabView;
import com.alsash.reciper.ui.fragment.RecipeTabCategoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A Tab View Presenter for tabs that represents recipe groups
 */
public class RecipeTabPresenter implements BasePresenter<RecipeTabView> {

    private static final boolean DRAW_TAB_TITLE_ON_HEADER = true;
    private static final List<SwipeTab> TABS = new ArrayList<>();

    static {
        TABS.add(new SwipeTab(R.string.tab_recipe_category, R.drawable.tab_recipe_category, false) {
            @Override
            public Fragment getFragment() {
                return RecipeTabCategoryFragment.newInstance();
            }
        });
        TABS.add(new SwipeTab(R.string.tab_recipe_grid, R.drawable.tab_recipe_grid, true) {
            @Override
            public Fragment getFragment() {
                return new Fragment();
            }
        });
        TABS.add(new SwipeTab(R.string.tab_recipe_label, R.drawable.tab_recipe_label, false) {
            @Override
            public Fragment getFragment() {
                return new Fragment();
            }
        });
        TABS.add(new SwipeTab(R.string.tab_recipe_bookmark, R.drawable.tab_recipe_bookmark, true) {
            @Override
            public Fragment getFragment() {
                return new Fragment();
            }
        });
    }

    private int startTabPosition;
    private boolean startTabPositionSet;

    public RecipeTabPresenter() {
    }

    public void onFabClick(RecipeTabView view) {
        view.showNotification("Message from presenter");
    }

    @Override
    public void attach(RecipeTabView view) {
        view.setDrawTabTitleOnHeader(DRAW_TAB_TITLE_ON_HEADER);
        view.setTabs(TABS);
    }

    @Override
    public void visible(RecipeTabView view) {
        if (startTabPositionSet) return;
        view.showTab(startTabPosition);
        startTabPositionSet = true;
    }

    @Override
    public void invisible(RecipeTabView view) {
        startTabPosition = view.shownTab();
    }

    @Override
    public void detach() {
        startTabPositionSet = false;
    }

    @Override
    public void refresh(RecipeTabView view) {
        detach();
        attach(view);
    }
}
