package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter extends BaseRecipeListPresenter<RecipeTabCategoryView, Category> {

    private static final int PAGINATION_LIMIT = 10;
    private static final int RELATIONS_LIMIT = 5;

    private final StorageApi storage;
    private final List<Category> categories = new ArrayList<>();


    public RecipeTabCategoryPresenter(StorageApi storage) {
        this.storage = storage;
    }

    @Override
    public void invisible(RecipeTabCategoryView view) {
        // Do nothing
    }

    @Override
    protected List<Category> getStart() {
        return categories;
    }

    @Override
    protected List<Category> loadNext() {
        return storage.getCategories(categories.size(), PAGINATION_LIMIT, RELATIONS_LIMIT);
    }
}
