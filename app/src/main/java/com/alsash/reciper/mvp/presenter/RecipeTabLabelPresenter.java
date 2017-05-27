package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabLabelView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Labels
 */
public class RecipeTabLabelPresenter extends BaseRecipeGroupPresenter<Label, RecipeTabLabelView> {

    private static final int PAGINATION_LABEL_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 10;

    private final StorageLogic storage;

    public RecipeTabLabelPresenter(StorageLogic storage) {
        super(PAGINATION_LABEL_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storage = storage;
    }

    @Override
    protected List<Label> loadNextGroups(int offset, int limit) {
        return null; // storage.getLabels(offset, limit);
    }

    @Override
    protected List<Recipe> loadNextRecipes(int offset, int limit, long labelId) {
        return null; // storage.getLabeledRecipes(offset, limit, labelId);
    }
}
