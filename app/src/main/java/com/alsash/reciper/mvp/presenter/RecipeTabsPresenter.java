package com.alsash.reciper.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.RecipeTabsView;
import com.alsash.reciper.ui.fragment.RecipeGroupListFragment;
import com.alsash.reciper.ui.fragment.RecipeSingleListFragment;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

public class RecipeTabsPresenter extends MvpBasePresenter<RecipeTabsView> {

    private static final int START_TAB_POSITION = 0; // Categories list

    private List<SwipeTab> tabs;

    public void loadTabs() {
        RecipeTabsView view = getView();
        if (view == null) return;
        view.setDrawTabTitleOnHeader(true);
        view.setupTabs(getTabs());
        view.showTab(START_TAB_POSITION);
    }

    /**
     * Define four tabs for RecipeTabsView
     *
     * @return list of predefined tabs
     */
    protected List<SwipeTab> getTabs() {
        if (tabs != null) return tabs;
        tabs = new ArrayList<>();
        // Tab 1 - Categories list (all recipes)
        tabs.add(new RecipeTab(RecipeGroupListFragment.newInstance(),
                R.string.tab_recipe_category,
                R.drawable.ic_category,
                false));
        // Tab 2 - Recipes (cards) list (all recipes)
        tabs.add(new RecipeTab(RecipeSingleListFragment.newInstance(),
                R.string.tab_recipe_category,
                R.drawable.ic_category,
                false));
        // Tab 3 - Labels list (recipes with labels only)
        tabs.add(new RecipeTab(RecipeSingleListFragment.newInstance(),
                R.string.tab_recipe_category,
                R.drawable.ic_category,
                false));
        // Tab 4 - Bookmarks list (recipes with bookmarks only)
        return tabs;
    }

    private static class RecipeTab implements SwipeTab {

        private final Fragment fragment;
        private final Integer titleRes;
        private final Integer iconRes;
        private final boolean hasNestedViews;

        public RecipeTab(Fragment fragment,
                         Integer titleRes,
                         Integer iconRes,
                         boolean hasNestedViews) {
            this.fragment = fragment;
            this.titleRes = titleRes;
            this.iconRes = iconRes;
            this.hasNestedViews = hasNestedViews;
        }

        @Override
        public boolean hasNestedViews() {
            return hasNestedViews;
        }

        @NonNull
        @Override
        public Fragment getFragment() {
            return fragment;
        }

        @Nullable
        @Override
        public Integer getTitle() {
            return titleRes;
        }

        @Nullable
        @Override
        public Integer getIcon() {
            return iconRes;
        }
    }
}
