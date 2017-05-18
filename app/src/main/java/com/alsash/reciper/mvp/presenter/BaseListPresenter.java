package com.alsash.reciper.mvp.presenter;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.alsash.reciper.mvp.view.BaseListView;

import org.reactivestreams.Publisher;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BooleanSupplier;
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

    private static final String TAG = "BaseListPresenter";

    private final PublishSubject<Integer> scrollSubject = PublishSubject.create();
    private final CompositeDisposable composite = new CompositeDisposable();
    private final List<M> models;
    private final int initialPosition;

    private Boolean fetched;
    private boolean loading;

    public BaseListPresenter() {
        this.models = getStart();
        this.initialPosition = models.size() - 1;
    }

    @UiThread
    protected abstract List<M> getStart();

    @WorkerThread
    protected abstract List<M> loadNext();

    @Override
    public void attach(V view) {
        view.setContainer(models);
        if (!isFetched()) {
            view.startPagination();
            fetch(new WeakReference<>(view));
        }
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

    public void onPagination(int maxVisiblePosition) {
        scrollSubject.onNext(maxVisiblePosition);
    }

    protected void fetch(final WeakReference<V> viewRef) {
        composite.add(scrollSubject
                .subscribeOn(AndroidSchedulers.mainThread()) // Run on the main thread by default
                .startWith(initialPosition) // Start the start load without scroll event
                .distinctUntilChanged()
                .map(new Function<Integer, Boolean>() {
                         @Override
                         public Boolean apply(@NonNull Integer visiblePosition) throws Exception {
                             return (!isFetched())
                                     && (!isLoading())
                                     && doLoading(visiblePosition);
                         }
                     }
                )
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@NonNull Boolean needLoading) throws Exception {
                        return needLoading;
                    }
                })
                .toFlowable(BackpressureStrategy.DROP)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean needLoading) throws Exception {
                        setLoading(true, viewRef); // needLoading is always true
                    }
                })
                .observeOn(Schedulers.io()) // Do loading on the background thread
                .concatMap(new Function<Boolean, Publisher<List<M>>>() {
                    @Override
                    public Publisher<List<M>> apply(@NonNull Boolean needLoading) throws Exception {
                        return Flowable.just(loadNext());
                    }
                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        return !isFetched();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // Observe results on the main thread
                .subscribeWith(new DisposableSubscriber<List<M>>() {
                                   @Override
                                   public void onNext(List<M> models) {
                                       setLoading(false, viewRef);
                                       if (models.size() > 0) {
                                           addNext(models, viewRef);
                                       } else {
                                           setFetched(true, viewRef);
                                       }
                                   }

                                   @Override
                                   public void onError(Throwable t) {
                                       Log.d(TAG, t.getMessage(), t);
                                       setLoading(false, viewRef);
                                   }

                                   @Override
                                   public void onComplete() {
                                       setLoading(false, viewRef);
                                   }
                               }
                ));
    }

    /**
     * Add next data to the container and notify view about it.
     *
     * @param models  - next data, size > 0.
     * @param viewRef - view that attached to this presenter
     */
    @NonNull
    protected void addNext(List<M> models, WeakReference<V> viewRef) {
        int insertPosition = this.models.size();
        this.models.addAll(models);
        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
            viewRef.get().showInsert(insertPosition);
        }
    }

    /**
     * Return data obtained in {@link #getStart()}
     *
     * @return data container
     */
    protected List<M> getModels() {
        return models;
    }

    /**
     * The decision whether to load a new pack of items.
     *
     * @param visiblePosition - current max visible position of items.
     * @return true if the load is needed.
     */
    protected boolean doLoading(int visiblePosition) {
        return ((visiblePosition + 1) * 2) >= models.size();
    }

    protected boolean isLoading() {
        return loading;
    }

    protected void setLoading(boolean loading, WeakReference<V> viewRef) {
        if (this.loading == loading) return;
        this.loading = loading;
        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
            viewRef.get().showLoading(loading);
        }
    }

    protected boolean isFetched() {
        return (fetched == null) ? false : fetched;
    }

    protected void setFetched(boolean fetched, WeakReference<V> viewRef) {
        if (this.fetched != null && this.fetched == fetched) return;
        this.fetched = fetched;
        if (viewRef.get() != null) {
            if (fetched) {
                viewRef.get().stopPagination();
            } else {
                viewRef.get().startPagination();
            }
        }
    }
}
