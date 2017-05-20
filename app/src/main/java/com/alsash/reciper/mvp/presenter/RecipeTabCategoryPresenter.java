package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter extends BaseListPresenter<Category, RecipeTabCategoryView> {

    private static final int PAGINATION_LIMIT = 5;

    private final StorageApi storage;

    public RecipeTabCategoryPresenter(StorageApi storage) {
        super(PAGINATION_LIMIT);
        this.storage = storage;
    }

    @Override
    protected List<Category> loadNext(int offset, int limit) {
        return storage.getCategories(offset, limit, 10);
    }
}
