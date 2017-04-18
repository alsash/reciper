package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.database.entity.DaoSession;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.database.CategoryMvpDb;
import com.alsash.reciper.mvp.view.CategoriesView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CategoriesPresenter extends MvpBasePresenter<CategoriesView> {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final DaoSession session;

    public CategoriesPresenter(DaoSession session) {
        this.session = session;
    }

    public void loadCategories() {
        disposables.add(getCategoriesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Category>>() {
                    @Override
                    public void onNext(List<Category> value) {
                        CategoriesView view = getView();
                        if (view == null) return;
                        view.addCategories(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void showCategories() {
        CategoriesView view = getView();
        if (view == null) return;
        view.showContent();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        disposables.clear();
    }

    private Observable<List<Category>> getCategoriesObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends List<Category>>>() {
            @Override
            public ObservableSource<? extends List<Category>> call() throws Exception {
                return Observable.just(getCategories());
            }
        });
    }

    private List<Category> getCategories() {
        List<Category> mvpCategories = new ArrayList<>();
        for (com.alsash.reciper.database.entity.Category categoryDb :
                session.getCategoryDao().loadAll()) {
            mvpCategories.add(new CategoryMvpDb(categoryDb));
        }
        return mvpCategories;
    }
}
