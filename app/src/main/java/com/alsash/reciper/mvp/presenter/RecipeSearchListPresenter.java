package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

import java.util.List;

/**
 * A Presenter that represents collection of all Recipes
 */
public class RecipeSearchListPresenter extends BaseRecipeListPresenter<RecipeListView> {

    private static final int PAGINATION_LIMIT = 15;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private String searchPattern;
    private String restrictionUuid;
    private Class<?> restrictionClass;

    public RecipeSearchListPresenter(StorageLogic storageLogic,
                                     BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, storageLogic, businessLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    public void onSearch(String pattern, RecipeListView view) {
        this.searchPattern = pattern;
        refresh(view);
    }

    public RecipeSearchListPresenter restrictByCategory(String categoryUuid) {
        this.restrictionUuid = categoryUuid;
        this.restrictionClass = Category.class;
        this.searchPattern = null;
        return this;
    }

    public RecipeSearchListPresenter restrictByLabel(String labelUuid) {
        this.restrictionUuid = labelUuid;
        this.restrictionClass = Label.class;
        this.searchPattern = null;
        return this;
    }

    public RecipeSearchListPresenter noRestriction() {
        this.restrictionUuid = null;
        this.restrictionClass = null;
        this.searchPattern = null;
        return this;
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return storageLogic.searchRecipes(
                searchPattern,
                restrictionUuid,
                restrictionClass,
                offset, limit);
    }
}
