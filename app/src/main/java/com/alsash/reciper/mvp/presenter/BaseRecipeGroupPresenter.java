package com.alsash.reciper.mvp.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.LongSparseArray;

import com.alsash.reciper.mvp.model.entity.BaseGroup;
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
public abstract class BaseRecipeGroupPresenter<G extends BaseGroup, V extends BaseListView<G>>
        extends BaseListPresenter<G, V> {

    private final int recipesLimit;
    private final Map<Long, List<Recipe>> prefetchedRecipes;
    private final LongSparseArray<BaseRecipeGroupedPresenter> presenters;

    @SuppressLint("UseSparseArrays")
    public BaseRecipeGroupPresenter(int groupsLimit, int recipesLimit) {
        super(groupsLimit);
        this.recipesLimit = recipesLimit;
        this.prefetchedRecipes = Collections.synchronizedMap(new HashMap<Long, List<Recipe>>());
        this.presenters = new LongSparseArray<>(recipesLimit * 7);
    }

    @WorkerThread
    protected abstract List<G> loadNextGroups(int offset, int limit);

    @WorkerThread
    protected abstract List<Recipe> loadNextRecipes(int offset, int limit, long groupId);

    @WorkerThread
    @Override
    protected List<G> loadNext(int offset, int limit) {
        List<G> groups = loadNextGroups(offset, limit);
        // Prefetch Recipes by groupId
        synchronized (prefetchedRecipes) {
            for (G group : groups) {
                long id = group.getId();
                prefetchedRecipes.put(id, loadNextRecipes(0, recipesLimit, id));
            }
        }
        return groups;
    }

    @UiThread
    public BaseRecipeGroupedPresenter getInnerPresenter(long groupId) {
        BaseRecipeGroupedPresenter presenter = presenters.get(groupId);
        if (presenter == null) {
            presenter = new BaseRecipeGroupedPresenter(recipesLimit, groupId) {
                @Override
                protected List<Recipe> loadNext(int offset, int limit) {
                    return loadNextRecipes(offset, limit, getGroupId());
                }
            };
            presenters.put(groupId, presenter);
            List<Recipe> recipes = prefetchedRecipes.get(groupId);
            if (recipes != null) presenter.getModels().addAll(recipes);
        }
        return presenter;
    }
}
