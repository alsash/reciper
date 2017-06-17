package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionLabelView;

import java.util.List;

/**
 * A Presenter that represents collection of a Recipes grouped by Labels
 */
public class RecipeCollectionLabelPresenter
        extends BaseRecipeGroupPresenter<Label, RecipeCollectionLabelView> {

    private static final int PAGINATION_LABEL_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    public RecipeCollectionLabelPresenter(StorageLogic storageLogic,
                                          BusinessLogic businessLogic) {
        super(PAGINATION_LABEL_LIMIT, PAGINATION_RECIPE_LIMIT);
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
    protected List<Label> loadNextGroups(int offset, int limit) {
        return storageLogic.getLabels(offset, limit);
    }

    @Override
    public List<Recipe> loadNextRecipes(Label label, int offset, int limit) {
        return storageLogic.getRecipes(label, offset, limit);
    }
}
