package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.database.entity.DaoSession;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.database.CategoryMvpDb;
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
    public void initView() {
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
        List<com.alsash.reciper.database.entity.Category> dbCategories =
                session.getCategoryDao().queryBuilder().build().forCurrentThread().list();
        for (com.alsash.reciper.database.entity.Category categoryDb : dbCategories) {
            CategoryMvpDb categoryMvpDb = new CategoryMvpDb(categoryDb); // Will prefetch items
            categories.add(categoryMvpDb);
        }
        return categories;
    }
}
