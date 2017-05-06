package com.alsash.reciper.mvp.presenter;

import android.content.Context;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter implements BasePresenter<RecipeTabCategoryView> {

    private final Context context;
    private final DaoSession session;
    private final List<Category> categories = new ArrayList<>();

    public RecipeTabCategoryPresenter(Context context, DaoSession session) {
        this.context = context;
        this.session = session;
    }

    @Override
    public void attach(RecipeTabCategoryView view) {

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
