package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabBookmarkView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Bookmarks
 */
public class RecipeTabBookmarkPresenter extends BaseListPresenter<Recipe, RecipeTabBookmarkView> {

    private static final int PAGINATION_LIMIT = 10;

    private final StorageApi storage;

    public RecipeTabBookmarkPresenter(StorageApi storage) {
        super(PAGINATION_LIMIT);
        this.storage = storage;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return storage.getBookmarkedRecipes(offset, limit);
    }
}
