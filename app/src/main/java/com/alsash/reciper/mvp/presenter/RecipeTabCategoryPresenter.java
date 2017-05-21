package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter extends BaseListPresenter<Category, RecipeTabCategoryView> {

    private static final int PAGINATION_CATEGORY_LIMIT = 5;
    private static final int PAGINATION_RECIPE_LIMIT = 10;

    private final StorageApi storage;

    public RecipeTabCategoryPresenter(StorageApi storage) {
        super(PAGINATION_CATEGORY_LIMIT);
        this.storage = storage;
    }

    public BaseRecipeGroupListPresenter getInnerPresenter() {
        return new BaseRecipeGroupListPresenter(PAGINATION_RECIPE_LIMIT) {
            @Override
            protected List<Recipe> loadNext(int offset, int limit) {
                return storage.getCategorizedRecipes(offset, limit, getGroupId());
            }
        };
    }

    @Override
    protected List<Category> loadNext(int offset, int limit) {
        return storage.getCategories(offset, limit);
    }
}
