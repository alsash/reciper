package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter extends BaseRecipeListPresenter<RecipeTabCategoryView> {

    private final DaoSession session;
    private final List<Category> categories = new ArrayList<>();

    private boolean fetched;

    public RecipeTabCategoryPresenter(DaoSession session) {
        this.session = session;
    }

    @Override
    public void attach(RecipeTabCategoryView view) {
        view.setCategories(categories);
        if (!fetched) fetch(view);
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

    public void onScroll(RecipeTabCategoryView view, int visibleCategoryPsition) {

    }

    private void fetch(RecipeTabCategoryView view) {

    }
}
