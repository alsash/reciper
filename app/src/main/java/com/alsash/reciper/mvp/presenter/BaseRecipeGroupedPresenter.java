package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

/**
 * An abstract presenter, that helps to represent inner list of Recipes
 */
public abstract class BaseRecipeGroupedPresenter
        extends BaseListPresenter<Recipe, RecipeListView> {

    private final long groupId;

    protected BaseRecipeGroupedPresenter(int limit, long groupId) {
        super(limit);
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }
}
