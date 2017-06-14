package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionGridView;

import java.util.List;

/**
 * A Presenter that represents collection of all Recipes
 */
public class RecipeCollectionGridPresenter
        extends BaseRecipeListPresenter<RecipeCollectionGridView> {

    private static final int PAGINATION_LIMIT = 15;

    private final StorageLogic storage;

    public RecipeCollectionGridPresenter(StorageLogic storage) {
        super(PAGINATION_LIMIT, storage);
        this.storage = storage;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return storage.getRecipes(offset, limit);
    }
}
