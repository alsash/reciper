package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;
import com.alsash.reciper.rx.RxPagination;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by alsash on 5/18/17.
 */
public class RecipeTabCategoryPresenter2 implements BasePresenter<RecipeTabCategoryView> {

    private static final int PAGINATION_LIMIT = 10;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final List<Category> categories = new ArrayList<>();
    private final StorageApi storage;

    private RxPagination<Category> categoryPagination;
    private List<RxPagination<Recipe>> recipePaginations; // by positions


    private boolean categoriesFetched;

    public RecipeTabCategoryPresenter(StorageApi storage) {
        this.storage = storage;
    }

    @Override
    public void attach(RecipeTabCategoryView view) {
        view.setCategories(categories);
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

    private void fetch(final WeakReference<RecipeTabCategoryView> viewRef) {
        categoryPagination = new RxPagination.Builder<Category>()
                .load(Flowable.create(new FlowableOnSubscribe<List<Category>>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<List<Category>> e) throws Exception {
                        storage.getCategories((int) e.requested(), 10, 10);
                    }
                }, BackpressureStrategy.BUFFER))
                .only(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return categories.size() <= (integer + 1) * 2;
                    }
                })
                .before(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
                            viewRef.get().showCategoriesLoading(true);
                        }
                    }
                })
                .into(new DisposableSubscriber<List<Category>>() {

                    @Override
                    protected void onStart() {
                        request(PAGINATION_LIMIT);
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        if ()
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }, compositeDisposable)
                .build();
    }

}
