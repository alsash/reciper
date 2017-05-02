package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.SwipeTabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeTabPresenter extends BasePresenter<SwipeTabView> {

    private static final int CATEGORIES_TAB_POSITION = 0;
    private static final int GRID_TAB_POSITION = 1;
    private static final int LABELS_TAB_POSITION = 2;
    private static final int BOOKMARKS_TAB_POSITION = 3;
    private static final int[] TAB_POSITIONS = new int[]{
            CATEGORIES_TAB_POSITION,
            GRID_TAB_POSITION,
            LABELS_TAB_POSITION,
            BOOKMARKS_TAB_POSITION
    };
    private static final List<SwipeTab> tabs = new ArrayList<>();

    static {
        Arrays.sort(TAB_POSITIONS);
    }

    private boolean firstTabShown;

    public RecipeTabPresenter() {
    }

    private static Fragment getRecipeTabFragment(int position) {
        switch (position) {
            case CATEGORIES_TAB_POSITION:
                return new Fragment();
            case GRID_TAB_POSITION:
                return new Fragment();
            case LABELS_TAB_POSITION:
                return new Fragment();
            case BOOKMARKS_TAB_POSITION:
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
                case CATEGORIES_TAB_POSITION:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_category,
                            R.drawable.ic_category,
                            false, i));
                    continue;
                case GRID_TAB_POSITION:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_list,
                            R.drawable.ic_all,
                            true, i));
                    continue;
                case LABELS_TAB_POSITION:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_label,
                            R.drawable.ic_labeled,
                            false, i));
                    continue;
                case BOOKMARKS_TAB_POSITION:
                    tabs.add(new RecipeTab(
                            R.string.tab_recipe_bookmark,
                            R.drawable.ic_bookmarked,
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
        if (!firstTabShown) {
            getView().showTab(TAB_POSITIONS[0]);
            firstTabShown = true;
        }
    }

    @Override
    protected void clear() {
        firstTabShown = false;
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
