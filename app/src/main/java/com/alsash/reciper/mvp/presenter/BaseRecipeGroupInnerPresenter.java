package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

import java.util.List;

/**
 * An inner presenter, that helps to represent inner list of Recipes
 *
 * @param <G> - Entity, that represents group of recipes
 */
public class BaseRecipeGroupInnerPresenter<G extends BaseEntity>
        extends BaseListPresenter<Recipe, RecipeListView> {

    private final BaseRecipeGroupPresenter<G, ?> outerPresenter;
    private final G group;

    public BaseRecipeGroupInnerPresenter(int limit,
                                         G group,
                                         BaseRecipeGroupPresenter<G, ?> outerPresenter) {
        super(limit);
        this.group = group;
        this.outerPresenter = outerPresenter;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return outerPresenter.loadNextRecipes(group, offset, limit);
    }
}
