package com.alsash.reciper.mvp.presenter;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.alsash.reciper.mvp.view.BaseListView;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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
                .subscribeOn(AndroidSchedulers.mainThread()) // Operate on the mainThread by default
                .startWith(-1) // Start the first load without scroll event. -1 means empty list
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
                .concatMap(new Function<Boolean, ObservableSource<List<M>>>() {
                    @Override
                    public ObservableSource<List<M>> apply(@NonNull Boolean b) throws Exception {
                        return new Observable<List<M>>() {
                            @Override
                            protected void subscribeActual(Observer<? super List<M>> observer) {
                                List<M> nextModels = loadNext();
                                if (nextModels.size() > 0) {
                                    observer.onNext(nextModels);
                                } else {
                                    observer.onComplete();
                                }
                            }
                        };
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // Observe results on the mainThread
                .subscribeWith(new DisposableObserver<List<M>>() {
                    @Override
                    public void onNext(List<M> nextModels) {
                        setLoading(false);
                        int insertPosition = addNext(nextModels);
                        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
                            viewRef.get().showLoading(false);
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
                        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
                            viewRef.get().showLoading(false);
                        }
                        dispose();
                    }
                }));
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

    /**
     * The decision whether to load a new pack of items.
     *
     * @param scrollPosition - current max visible position of items.
     *                       -1 when there are no items
     * @return true if the load is needed.
     */
    protected boolean doLoading(int scrollPosition) {
        return models.size() <= (scrollPosition + 1) * 2;
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
