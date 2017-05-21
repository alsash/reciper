package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

/**
 * An abstract presenter, that helps to represent inner list of Recipes
 */
public abstract class BaseRecipeGroupedPresenter
        extends BaseListPresenter<Recipe, RecipeListView> {

    private final long groupId;
    private boolean doFirstTime = true;

    protected BaseRecipeGroupedPresenter(int limit, long groupId) {
        super(limit);
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    @Override
    protected boolean doLoading(int visiblePosition) {
        boolean doLoading = super.doLoading(visiblePosition);
        if (doLoading && doFirstTime) {
            doLoading = (getModels().size() == 0); // no prefetched items
            doFirstTime = false;
        }
        return doLoading;
    }
}
