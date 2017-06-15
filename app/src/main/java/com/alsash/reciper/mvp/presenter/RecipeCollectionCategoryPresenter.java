package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
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

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    public RecipeCollectionCategoryPresenter(StorageLogic storageLogic,
                                             BusinessLogic businessLogic) {
        super(PAGINATION_CATEGORY_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    protected StorageLogic getStorageLogic() {
        return storageLogic;
    }

    @Override
    protected BusinessLogic getBusinessLogic() {
        return businessLogic;
    }

    @Override
    protected List<Category> loadNextGroups(int offset, int limit) {
        return storageLogic.getCategories(offset, limit);
    }

    @Override
    public List<Recipe> loadNextRecipes(Category category, int offset, int limit) {
        return storageLogic.getRecipes(category, offset, limit);
    }

    public void changeFavorite(final Recipe recipe) {
        storageLogic.updateSync(recipe, RecipeAction.FAVORITE);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateAsync(recipe);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }
}
