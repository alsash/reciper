package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeTabCategoryPresenter extends BaseRecipeListPresenter<RecipeTabCategoryView> {

    private final DaoSession session;
    private final List<Category> categories = new ArrayList<>();

    private PublishSubject<Integer> scrollSubject = PublishSubject.create();

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

    public void onScroll(int visibleCategoryPosition) {
        if (!scrollSubject.hasObservers()
                || scrollSubject.hasComplete()
                || scrollSubject.hasThrowable()) {
            return;
        }
        scrollSubject.onNext(visibleCategoryPosition);
    }

    private void fetch(RecipeTabCategoryView view) {
        scrollSubject
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribeWith(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
