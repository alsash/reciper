package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionCategoryView;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeCollectionCategoryPresenter
        extends BaseRecipeGroupPresenter<Category, RecipeCollectionCategoryView> {

    private static final int PAGINATION_CATEGORY_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 10;

    private final StorageLogic storage;

    public RecipeCollectionCategoryPresenter(StorageLogic storage) {
        super(PAGINATION_CATEGORY_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storage = storage;
    }

    @Override
    protected StorageLogic getStorageLogic() {
        return storage;
    }

    @Override
    protected List<Category> loadNextGroups(int offset, int limit) {
        return storage.getCategories(offset, limit);
    }

    @Override
    public List<Recipe> loadNextRecipes(Category category, int offset, int limit) {
        return storage.getRecipes(category, offset, limit);
    }

    public void changeFavorite(final Recipe recipe) {
        storage.updateSync(recipe, RecipeAction.FAVORITE);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storage.updateAsync(recipe);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }
}
