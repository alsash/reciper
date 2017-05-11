package com.alsash.reciper.mvp.presenter;

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
        if (!isFetched()) fetch(new WeakReference<>(view));
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

    protected void fetch(final WeakReference<V> viewRef) {
        composite.add(scrollSubject
                .subscribeOn(AndroidSchedulers.mainThread()) // Position in the chain doesn't matter
                .distinctUntilChanged()
                .map(new Function<Integer, Boolean>() {
                         @Override
                         public Boolean apply(@NonNull Integer scrollPosition) throws Exception {
                             return (!isFetched())
                                     && (!isLoading())
                                     && doLoading(scrollPosition);
                         }
                     }
                )
                .distinctUntilChanged()
                .skipWhile(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@NonNull Boolean needLoading) throws Exception {
                        return !needLoading;
                    }
                })
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean needLoading) throws Exception {
                        setLoading(true); // needLoading is always true
                        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
                            viewRef.get().showLoading(true);
                        }
                    }
                          }
                )
                .observeOn(Schedulers.io()) // Next operations will be done on background thread
                .toFlowable(BackpressureStrategy.DROP)
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
                    @Override
                    public void onNext(List<M> nextModels) {
                        setLoading(false);
                        int insertPosition = addNext(nextModels);
                        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
                            viewRef.get().showInsert(insertPosition);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, t.getMessage(), t);
                    }

                    @Override
                    public void onComplete() {
                        setLoading(false);
                        setFetched(true);
                    }
                               }
                ));
        // Start first loading
        if ((!isFetched()) && (!isLoading()) && models.size() == initialSize) {
            scrollSubject.onNext(initialSize);
        }
    }

    /**
     * Add next data to container
     *
     * @param models next data
     * @return insert position
     */
    protected int addNext(List<M> models) {
        int insertPosition = this.models.size();
        this.models.addAll(models);
        return insertPosition;
    }

    /**
     * Return data obtained in {@link #getStart()}
     *
     * @return data container
     */
    protected List<M> getModels() {
        return models;
    }

    protected boolean doLoading(int scrollPosition) {
        return models.size() < scrollPosition * 2;
    }

    protected boolean isLoading() {
        return loading;
    }

    protected void setLoading(boolean loading) {
        this.loading = loading;
    }

    protected boolean isFetched() {
        return fetched;
    }

    protected void setFetched(boolean fetched) {
        this.fetched = fetched;
    }
}
