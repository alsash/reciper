package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabGridView;

import java.util.List;

/**
 * A Presenter that represents collection of all Recipes
 */
public class RecipeTabGridPresenter extends BaseListPresenter<Recipe, RecipeTabGridView> {

    private static final int PAGINATION_LIMIT = 10;

    private final StorageApi storage;

    public RecipeTabGridPresenter(StorageApi storage) {
        super(PAGINATION_LIMIT);
        this.storage = storage;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return storage.getRecipes(offset, limit);
    }
}
