package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.RecipeTabView;
import com.alsash.reciper.ui.fragment.RecipeCategoriesFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeTabPresenter extends BasePresenter<RecipeTabView> {

    private static final int START_TAB_POSITION = 0; // Categories list
    private static final boolean DRAW_TAB_TITLE_ON_HEADER = true;

    private final List<SwipeTab> tabs = new ArrayList<>();

    public RecipeTabPresenter() {
        // Empty constructor
    }

    private static Fragment getRecipeTabFragment(int position) {
        switch (position) {
            case 0:
                return RecipeCategoriesFragment.newInstance();
            case 1:
                return new Fragment();
            case 2:
                return new Fragment();
            case 3:
                return new Fragment();
            default:
                throw new IllegalArgumentException("Tab at " + position + "is unknown");
        }
    }

    @Override
    public void initView() {
        RecipeTabView view = getView();
        if (view == null) return;
        view.setDrawTabTitleOnHeader(DRAW_TAB_TITLE_ON_HEADER);
        view.setTabs(getTabs());
    }

    @Override
    public void completeView() {
        RecipeTabView view = getView();
        if (view == null) return;
        view.showTab(START_TAB_POSITION);
    }

    /**
     * Define four tabs for RecipeTabView
     *
     * @return list of predefined tabs
     */
    private List<SwipeTab> getTabs() {
        if (tabs.size() > 0) return tabs;
        // Tab 0 - Categories list (all recipes)
        tabs.add(new RecipeTab(0,
                R.string.tab_recipe_category,
                R.drawable.ic_category,
                true));
        // Tab 1 - Recipes (cards) list (all recipes)
        tabs.add(new RecipeTab(1,
                R.string.tab_recipe_list,
                R.drawable.ic_all,
                false));
        // Tab 2 - Labels list (recipes with labels only)
        tabs.add(new RecipeTab(2,
                R.string.tab_recipe_label,
                R.drawable.ic_labeled,
                true));
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
