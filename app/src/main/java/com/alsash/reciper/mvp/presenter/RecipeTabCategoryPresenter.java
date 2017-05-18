package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter implements BasePresenter<RecipeTabCategoryView> {

    private static final int PAGINATION_LIMIT = 5;
    private static final int RELATION_LIMIT = 10; // Limit for related entities

    private final StorageApi storage;
    private final List<Category> categories = getModels(); // see getStart()

    private int count;

    public RecipeTabCategoryPresenter(StorageApi storage) {
        this.storage = storage;
    }

    @Override
    public void invisible(RecipeTabCategoryView view) {
        // Do nothing
    }

    @Override
    protected List<Category> getStart() {
        return new ArrayList<>(); // categories = new ArrayList<>();
    }

    @Override
    protected List<Category> loadNext() {
        return storage.getCategories(categories.size(), PAGINATION_LIMIT, RELATION_LIMIT);
    }
}
