package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.api.storage.local.database.table.LabelTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeTable;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.database.CategoryMvpDb;
import com.alsash.reciper.mvp.model.entity.database.LabelMvpDb;
import com.alsash.reciper.mvp.model.entity.database.RecipeMvpDb;
import com.alsash.reciper.mvp.view.RecipeCategoriesView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class RecipeCategoriesPresenter extends BaseRecipesPresenter<RecipeCategoriesView> {

    private final DaoSession session;
    private final List<Category> categories = new ArrayList<>();
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    public RecipeCategoriesPresenter(DaoSession session) {
        this.session = session;
    }

    @Override
    public void init() {
        if (getView() == null) return;
        loadCategories();
    }

    @Override
    public void completeView() {
        if (getView() == null) return;
        getView().setCategories(categories);
        getView().showCategories();
    }

    @Override
    public void setView(@Nullable RecipeCategoriesView view) {
        super.setView(view);
        if (view == null) { // DetachView
            compositeSubscription.unsubscribe();
            compositeSubscription.clear();
        }
    }

    private void loadCategories() {
        compositeSubscription.add(
                Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return getCategories().size();
                    }
                }).observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer size) {
                                if (size > 0) completeView();
                            }
                        })
        );
    }

    private synchronized List<Category> getCategories() {
        if (categories.size() > 0) return categories;
        List<CategoryTable> dbCategories = session.getCategoryTableDao().loadAll();

        for (CategoryTable categoryDb : dbCategories) {
            List<Recipe> categoryRecipes = new ArrayList<>();
            categories.add(new CategoryMvpDb(categoryDb.getId(), categoryDb.getName(),
                    categoryRecipes));

            for (RecipeTable recipeDb : categoryDb.getRecipes()) {

                List<Label> recipeLabels = new ArrayList<>();
                categoryRecipes.add(new RecipeMvpDb(recipeDb.getId(), recipeDb.getName(),
                        categories.get(categories.size() - 1), recipeLabels));

                for (LabelTable labelDb : recipeDb.getLabels()) {
                    recipeLabels.add(new LabelMvpDb(labelDb.getId(), labelDb.getName(),
                            new ArrayList<Recipe>())); // Labels without inner recipes
                }
            }
        }
        return categories;
    }
}
