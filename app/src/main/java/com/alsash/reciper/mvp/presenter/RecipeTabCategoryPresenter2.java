package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alsash on 5/18/17.
 */
public class RecipeTabCategoryPresenter2 implements BasePresenter<RecipeTabCategoryView> {

    private final List<Category> categories = new ArrayList<>();

    private boolean categoriesFetched;

    @Override
    public void attach(RecipeTabCategoryView view) {
        view.setCategories(categories);
        view.setCategoriesFetched(categoriesFetched);
        view.setRecipesFetched(0, false);
    }

    @Override
    public void visible(RecipeTabCategoryView view) {

    }

    @Override
    public void invisible(RecipeTabCategoryView view) {

    }

    @Override
    public void detach() {

    }
}
