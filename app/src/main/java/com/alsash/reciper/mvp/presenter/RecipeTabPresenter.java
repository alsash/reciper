package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.RecipeTabView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

public class RecipeTabPresenter extends MvpBasePresenter<RecipeTabView> {

    private static final int START_TAB_POSITION = 0; // Categories list

    private static Fragment getRecipeTabFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment();
            case 1:
                return RecipeSingleListFragment.newInstance();
            case 2:
                return new Fragment();
            case 3:
                return RecipeSingleListFragment.newInstance();
            default:
                throw new IllegalArgumentException("Tab at " + position + "is unknown");
        }
    }

    public void loadTabs() {
        RecipeTabView view = getView();
        if (view == null) return;
        view.setDrawTabTitleOnHeader(true);
        view.setTabs(getTabs());
    }

    public void showTabs() {
        RecipeTabView view = getView();
        if (view == null) return;
        view.showTab(START_TAB_POSITION, false);
    }

    /**
     * Define four tabs for RecipeTabView
     *
     * @return list of predefined tabs
     */
    private List<SwipeTab> getTabs() {
        List<SwipeTab> tabs = new ArrayList<>();
        // Tab 0 - Categories list (all recipes)
        tabs.add(new RecipeTab(0,
                R.string.tab_recipe_category,
                R.drawable.ic_category,
                false));
        // Tab 1 - Recipes (cards) list (all recipes)
        tabs.add(new RecipeTab(1,
                R.string.tab_recipe_list,
                R.drawable.ic_all,
                false));
        // Tab 2 - Labels list (recipes with labels only)
        tabs.add(new RecipeTab(2,
                R.string.tab_recipe_label,
                R.drawable.ic_labeled,
                false));
        // Tab 3 - Bookmarks list (recipes with bookmarks only)
        tabs.add(new RecipeTab(3,
                R.string.tab_recipe_bookmark,
                R.drawable.ic_bookmarked,
                false));
        return tabs;
    }

    private static class RecipeTab implements SwipeTab {
        private final int fragmentPosition;
        private final int titleRes;
        private final int iconRes;
        private final boolean hasNestedViews;

        RecipeTab(int fragmentPosition, int titleRes, int iconRes, boolean hasNestedViews) {
            this.fragmentPosition = fragmentPosition;
            this.titleRes = titleRes;
            this.iconRes = iconRes;
            this.hasNestedViews = hasNestedViews;
        }

        @Override
        public boolean hasNestedViews() {
            return hasNestedViews;
        }

        @Override
        public Fragment getFragment() {
            return getRecipeTabFragment(fragmentPosition);
        }

        @Override
        public Integer getTitle() {
            return titleRes;
        }

        @Override
        public Integer getIcon() {
            return iconRes;
        }
    }
}
