package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabLabelView;

import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Labels
 */
public class RecipeTabLabelPresenter extends BaseListPresenter<Label, RecipeTabLabelView> {

    private static final int PAGINATION_LABEL_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 10;

    private final StorageApi storage;

    public RecipeTabLabelPresenter(StorageApi storage) {
        super(PAGINATION_LABEL_LIMIT);
        this.storage = storage;
    }

    public BaseRecipeGroupListPresenter getInnerPresenter() {
        return new BaseRecipeGroupListPresenter(PAGINATION_RECIPE_LIMIT) {
            @Override
            protected List<Recipe> loadNext(int offset, int limit) {
                return storage.getLabeledRecipes(offset, limit, getGroupId());
            }
        };
    }

    @Override
    protected List<Label> loadNext(int offset, int limit) {
        return storage.getLabels(offset, limit);
    }
}
