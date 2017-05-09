package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.alsash.reciper.mvp.view.BaseListView;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Base List Presenter, that fetch a data, represented by Model,
 * and holds BaseListView instance, witch represents a Model of a data.
 */
public abstract class BaseListPresenter<V extends BaseListView<M>, M> implements BasePresenter<V> {

    private static final String TAG = BaseListPresenter.class.getCanonicalName();

    private final PublishSubject<Integer> scrollSubject = PublishSubject.create();
    private final CompositeDisposable composite = new CompositeDisposable();
    private final List<M> models;
    private final int initialSize;

    private boolean loading;
    private boolean fetched;

    public BaseListPresenter() {
        this.models = getStart();
        this.initialSize = models.size();
    }

    @UiThread
    protected abstract List<M> getStart();

    @WorkerThread
    protected abstract List<M> loadNext();

    @Override
    public void attach(V view) {
        view.setContainer(models);
        if (!isFetched()) fetch(view);
    }

    @Override
    public void visible(V view) {
        view.showLoading(isLoading());
    }

    @Override
    public void detach() {
        composite.dispose(); // set Observers to null, so they are not holds any shadow references
        composite.clear();   // in v.2.1.0 - same as dispose(), but without set isDispose()
    }

    public void onScroll(int scrollPosition) {
        if (!scrollSubject.hasObservers() || scrollSubject.hasComplete()) return;
        scrollSubject.onNext(scrollPosition);
    }

    @UiThread
    protected void addNext(List<M> models, @Nullable V view) {
        int insertPosition = this.models.size();
        this.models.addAll(models);
        if (view == null) return;
        if (view.isViewVisible()) view.showInsert(insertPosition);
    }

    @UiThread
    protected boolean doLoading(int scrollPosition, @Nullable V view) {
        return models.size() < scrollPosition * 2;
    }

    @UiThread
    protected boolean isLoading() {
        return loading;
    }

    @UiThread
    protected void setLoading(boolean loading, @Nullable V view) {
        this.loading = loading;
        if (view == null) return;
        if (view.isViewVisible()) view.showLoading(loading);
    }

    @UiThread
    protected boolean isFetched() {
        return fetched;
    }

    @UiThread
    protected void setFetched(boolean fetched, @Nullable V view) {
        this.fetched = fetched;
    }

    private void fetch(V view) {
        composite.add(scrollSubject
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .map(new Function<Integer, Boolean>() {
                            private WeakReference<V> viewRef;

                            public Function<Integer, Boolean> setView(V view) {
                                viewRef = new WeakReference<>(view);
                                return this;
                            }

                            @Override
                            public Boolean apply(@NonNull Integer scrollPosition) throws Exception {
                                return (!isFetched())
                                        && (!isLoading())
                                        && doLoading(scrollPosition, viewRef.get());
                            }
                        }.setView(view)
                )
                .distinctUntilChanged()
                .skipWhile(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@NonNull Boolean needLoading) throws Exception {
                        return !needLoading;
                    }
                })
                .doOnNext(new Consumer<Boolean>() {
                            private WeakReference<V> viewRef;

                            public Consumer<Boolean> setView(V view) {
                                viewRef = new WeakReference<>(view);
                                return this;
                            }

                            @Override
                            public void accept(@NonNull Boolean needLoading) throws Exception {
                                setLoading(true, viewRef.get()); // needLoading is always true
                            }
                        }.setView(view)
                )
                .toFlowable(BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .concatMap(new Function<Boolean, Publisher<List<M>>>() {
                    @Override
                    public Publisher<List<M>> apply(@NonNull Boolean aBoolean) throws Exception {
                        return new Publisher<List<M>>() {
                            @Override
                            public void subscribe(Subscriber<? super List<M>> s) {
                                List<M> nextModels = loadNext();
                                if (nextModels.size() > 0) {
                                    s.onNext(nextModels);
                                } else {
                                    s.onComplete();
                                }
                            }
                        };
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<M>>() {
                            private WeakReference<V> viewRef;

                            public DisposableSubscriber<List<M>> setView(V view) {
                                viewRef = new WeakReference<>(view);
                                return this;
                            }

                            @Override
                            public void onNext(List<M> nextModels) {
                                setLoading(false, viewRef.get());
                                addNext(nextModels, viewRef.get());
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, t.getMessage(), t);
                            }

                            @Override
                            public void onComplete() {
                                setFetched(true, viewRef.get());
                                dispose();
                            }
                        }.setView(view)
                ));

        // Start loading without scroll
        if ((!isFetched()) && (!isLoading()) && models.size() == initialSize) {
            scrollSubject.onNext(initialSize);
        }
    }
}
