package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.di.scope.RecipeTabScope;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.SwipeTabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RecipeTabScope
public class RecipeTabPresenter extends BasePresenter<SwipeTabView> {

    private static final int TAB_POSITION_CATEGORY = 0;
    private static final int TAB_POSITION_GRID = 1;
    private static final int TAB_POSITION_LABEL = 2;
    private static final int TAB_POSITION_BOOKMARK = 3;
    private static final int[] TAB_POSITIONS = new int[]{
            TAB_POSITION_CATEGORY,
            TAB_POSITION_GRID,
            TAB_POSITION_LABEL,
            TAB_POSITION_BOOKMARK
    };
    private static final List<SwipeTab> tabs = new ArrayList<>();

    static {
        Arrays.sort(TAB_POSITIONS);
    }

    private int currentTabPosition = TAB_POSITIONS[0];
    private boolean currentTabWasShown;

    private static Fragment getRecipeTabFragment(int position) {
        switch (position) {
            case TAB_POSITION_CATEGORY:
                return new Fragment();
            case TAB_POSITION_GRID:
                return new Fragment();
            case TAB_POSITION_LABEL:
                return new Fragment();
            case TAB_POSITION_BOOKMARK:
                return new Fragment();
            default:
                throw new IllegalArgumentException("Tab at " + position + "is unknown");
        }
    }

    /**
     * Define four tabs for SwipeTabView
     *
     * @return list of predefined tabs
     */
    private static List<SwipeTab> getTabs() {
        if (tabs.size() > 0) return tabs;
        for (int i : TAB_POSITIONS) {
            switch (i) {
                case TAB_POSITION_CATEGORY:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_category,
                            R.drawable.tab_recipe_category,
                            false, i));
                    continue;
                case TAB_POSITION_GRID:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_grid,
                            R.drawable.tab_recipe_grid,
                            true, i));
                    continue;
                case TAB_POSITION_LABEL:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_label,
                            R.drawable.tab_recipe_label,
                            false, i));
                    continue;
                case TAB_POSITION_BOOKMARK:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_bookmark,
                            R.drawable.tab_recipe_bookmark,
                            true, 3));
            }
        }
        return tabs;
    }

    @Override
    protected void init() {
        if (getView() == null) return;
        getView().setDrawTabTitleOnHeader(true);
        getView().setTabs(getTabs());
        setInitialized(true);
    }

    @Override
    protected void show() {
        if (getView() == null) return;
        if (!currentTabWasShown) {
            getView().showTab(currentTabPosition);
            currentTabWasShown = true;
        }
    }

    @Override
    protected void clear() {
        currentTabWasShown = false;
        if (getView() != null) currentTabPosition = getView().getShownTab();
    }

    private static class RecipeTab extends SwipeTab {
        private final int fragmentPosition;

        public RecipeTab(Integer title, Integer icon, boolean isSwiped, int fragmentPosition) {
            super(title, icon, isSwiped);
            this.fragmentPosition = fragmentPosition;
        }

        @Override
        public Fragment getFragment() {
            return getRecipeTabFragment(fragmentPosition);
        }
    }
}
