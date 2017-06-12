package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

/**
 * An abstract presenter, that helps to represent inner list of Recipes
 *
 * @param <G> - Entity, that represents group of recipes
 */
public abstract class BaseRecipeGroupInnerPresenter<G extends BaseEntity>
        extends BaseListPresenter<Recipe, RecipeListView> {

    private final G group;
    private boolean checkPrefetched;

    protected BaseRecipeGroupInnerPresenter(int limit, G group) {
        super(limit);
        this.group = group;
    }

    public G getGroup() {
        return group;
    }

    @Override
    protected boolean doLoading(int visiblePosition) {
        boolean doLoading = super.doLoading(visiblePosition);
        if (doLoading && !checkPrefetched) {
            doLoading = (getModels().size() == 0); // no prefetched items
            checkPrefetched = true;
        }
        return doLoading;
    }
}
