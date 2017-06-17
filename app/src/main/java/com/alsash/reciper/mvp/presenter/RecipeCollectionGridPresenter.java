package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionGridView;

import java.util.List;

/**
 * A Presenter that represents collection of all Recipes
 */
public class RecipeCollectionGridPresenter
        extends BaseRecipeListPresenter<RecipeCollectionGridView> {

    private static final int PAGINATION_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    public RecipeCollectionGridPresenter(StorageLogic storageLogic,
                                         BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, storageLogic, businessLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return storageLogic.getRecipes(offset, limit);
    }
}
