package com.alsash.reciper.mvp.presenter;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.mvp.view.RecipeListView;

import java.lang.ref.WeakReference;
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
    private final Map<G, RecipeGroupInnerPresenter<G>> presenters;

    public BaseRecipeGroupPresenter(int groupsLimit, int recipesLimit) {
        super(groupsLimit);
        this.recipesLimit = recipesLimit;
        this.prefetchedRecipes = Collections.synchronizedMap(
                new HashMap<G, List<Recipe>>());
        this.presenters = Collections.synchronizedMap(
                new HashMap<G, RecipeGroupInnerPresenter<G>>());
    }

    protected abstract StorageLogic getStorageLogic();

    protected abstract BusinessLogic getBusinessLogic();

    @WorkerThread
    protected abstract List<G> loadNextGroups(int offset, int limit);

    @WorkerThread
    protected abstract List<Recipe> loadNextRecipes(G group, int offset, int limit);

    public boolean canOpenGroup(G group) {
        return group.getId() == null || presenters.get(group).getModels().size() > 0;
    }

    @Override
    public void refresh(V view) {
        prefetchedRecipes.clear();
        presenters.clear();
        super.refresh(view);
    }

    @Override
    protected void refresh() {
        prefetchedRecipes.clear();
        presenters.clear();
        super.refresh();
    }

    @Override
    protected void clear(WeakReference<V> viewRef) {
        prefetchedRecipes.clear();
        presenters.clear();
        super.clear(viewRef);
    }

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
    public BaseRecipeListPresenter<RecipeListView> getInnerPresenter(G group) {
        RecipeGroupInnerPresenter<G> presenter = presenters.get(group);
        if (presenter == null) {
            presenter = new RecipeGroupInnerPresenter<>(recipesLimit, group, this);
            presenters.put(group, presenter);
            List<Recipe> recipes = prefetchedRecipes.get(group);
            if (recipes != null) presenter.getModels().addAll(recipes);
        }
        return presenter;
    }

    /**
     * An inner presenter, that helps to represent inner list of Recipes
     *
     * @param <G> - Entity, that represents group of recipes
     */
    private static class RecipeGroupInnerPresenter<G extends BaseEntity>
            extends BaseRecipeListPresenter<RecipeListView> {

        private final BaseRecipeGroupPresenter<G, ?> outerPresenter;
        private final G group;

        public RecipeGroupInnerPresenter(int limit,
                                         G group,
                                         BaseRecipeGroupPresenter<G, ?> outerPresenter) {
            super(limit, outerPresenter.getStorageLogic(), outerPresenter.getBusinessLogic());
            this.group = group;
            this.outerPresenter = outerPresenter;
        }

        @Override
        protected List<Recipe> loadNext(int offset, int limit) {
            return outerPresenter.loadNextRecipes(group, offset, limit);
        }
    }
}
