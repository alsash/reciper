package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabBookmarkView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Bookmarks
 */
public class RecipeTabBookmarkPresenter extends BaseListPresenter<Recipe, RecipeTabBookmarkView> {

    private static final int PAGINATION_LIMIT = 10;

    private final StorageLogic storage;

    public RecipeTabBookmarkPresenter(StorageLogic storage) {
        super(PAGINATION_LIMIT);
        this.storage = storage;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return null; // storage.getBookmarkedRecipes(offset, limit);
    }
}
