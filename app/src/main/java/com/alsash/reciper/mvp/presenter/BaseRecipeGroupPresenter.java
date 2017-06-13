package com.alsash.reciper.mvp.presenter;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseListView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract presenter that represents list of recipes, grouped by any id
 *
 * @param <G> - Entity, that represents group of recipes
 * @param <V> - view, that can be attached to this presenter
 */
public abstract class BaseRecipeGroupPresenter<G extends BaseEntity, V extends BaseListView<G>>
        extends BaseListPresenter<G, V> {

    private final int recipesLimit;
    private final Map<G, List<Recipe>> prefetchedRecipes;
    private final Map<G, BaseRecipeGroupInnerPresenter<G>> presenters;

    public BaseRecipeGroupPresenter(int groupsLimit, int recipesLimit) {
        super(groupsLimit);
        this.recipesLimit = recipesLimit;
        this.prefetchedRecipes = Collections.synchronizedMap(
                new HashMap<G, List<Recipe>>());
        this.presenters = Collections.synchronizedMap(
                new HashMap<G, BaseRecipeGroupInnerPresenter<G>>());
    }

    @WorkerThread
    protected abstract List<G> loadNextGroups(int offset, int limit);

    @WorkerThread
    public abstract List<Recipe> loadNextRecipes(G group, int offset, int limit);


    @WorkerThread
    @Override
    protected List<G> loadNext(int offset, int limit) {
        List<G> groups = loadNextGroups(offset, limit);
        // Prefetch Recipes by group
        synchronized (prefetchedRecipes) {
            for (G group : groups) {
                prefetchedRecipes.put(group, loadNextRecipes(group, 0, recipesLimit));
            }
        }
        return groups;
    }

    @UiThread
    public BaseRecipeGroupInnerPresenter<G> getInnerPresenter(G group) {
        BaseRecipeGroupInnerPresenter<G> presenter = presenters.get(group);
        if (presenter == null) {
            presenter = new BaseRecipeGroupInnerPresenter<>(recipesLimit, group, this);
            presenters.put(group, presenter);
            List<Recipe> recipes = prefetchedRecipes.get(group);
            if (recipes != null) presenter.getModels().addAll(recipes);
        }
        return presenter;
    }
}
