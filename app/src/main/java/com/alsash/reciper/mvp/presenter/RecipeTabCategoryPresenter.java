package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter
        extends BaseRecipeGroupPresenter<Category, RecipeTabCategoryView> {

    private static final int PAGINATION_CATEGORY_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 10;

    private final StorageApi storage;

    public RecipeTabCategoryPresenter(StorageApi storage) {
        super(PAGINATION_CATEGORY_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storage = storage;
    }

    @Override
    protected List<Category> loadNextGroups(int offset, int limit) {
        return storage.getCategories(offset, limit);
    }

    @Override
    protected List<Recipe> loadNextRecipes(int offset, int limit, long categoryId) {
        return storage.getCategorizedRecipes(offset, limit, categoryId);
    }
}
