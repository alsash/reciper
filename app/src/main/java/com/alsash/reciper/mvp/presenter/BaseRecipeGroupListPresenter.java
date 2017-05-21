package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

import java.util.concurrent.atomic.AtomicLong;

/**
 * An abstract presenter, that help to represent inner list of Recipes
 */
public abstract class BaseRecipeGroupListPresenter
        extends BaseListPresenter<Recipe, RecipeListView> {

    private final AtomicLong groupId = new AtomicLong(-1);

    protected BaseRecipeGroupListPresenter(int limit) {
        super(limit);
    }

    public long getGroupId() {
        return groupId.get();
    }

    public void setGroupId(long groupId) {
        this.groupId.set(groupId);
    }
}
