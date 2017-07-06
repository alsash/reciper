package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.view.BaseListView;

import org.reactivestreams.Publisher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
public abstract class BaseListPresenter<M extends BaseEntity, V extends BaseListView<M>>
        implements BasePresenter<V> {

    private static final String TAG = "BaseListPresenter";
    private static final int DEFAULT_LIMIT = 10;

    private final PublishSubject<Integer> scrollSubject = PublishSubject.create();
    private final List<M> models = new ArrayList<>();
    private final int limit;

    private CompositeDisposable composite = new CompositeDisposable();
    private boolean fetched;
    private boolean loading;
    private int previousPosition;

    public BaseListPresenter(int limit) {
        this.limit = (limit > 0) ? limit : DEFAULT_LIMIT;
    }

    @WorkerThread
    protected abstract List<M> loadNext(int offset, int limit);

    @Override
    public void attach(V view) {
        WeakReference<V> viewRef = new WeakReference<>(view);
        resetPreviousPosition();
        view.setContainer(models);
        view.setPagination(!isFetched());
        if (!isFetched()) fetch(viewRef);
    }

    @Override
    public void visible(V view) {
        view.showUpdate();
        view.showLoading(isLoading());
    }

    @Override
    public void invisible(V view) {
        resetPreviousPosition();
    }

    @Override
    public void detach() {
        composite.dispose(); // set Observers to null, so they are not holds any shadow references
        composite.clear();   // in v.2.1.0 - same as dispose(), but without set isDispose()
        composite = new CompositeDisposable(); // Recreate Composite for correct refreshing
        setLoading(false);
    }

    @Override
    public void refresh(V view) {
        detach();
        models.clear();
        resetPreviousPosition();
        WeakReference<V> viewRef = new WeakReference<>(view);
        setFetched(false, viewRef);
        setLoading(false, viewRef);
        attach(view);
    }

    protected void refresh() {
        detach();
        models.clear();
        resetPreviousPosition();
        setFetched(false);
        setLoading(false);
    }

    /**
     * Notify about scroll event for doing pagination
     *
     * @param maxVisiblePosition - position of the last visible item
     */
    public void nextPagination(int maxVisiblePosition) {
        scrollSubject.onNext(maxVisiblePosition);
    }

    protected void fetch(final WeakReference<V> viewRef) {
        composite.add(scrollSubject
                .subscribeOn(AndroidSchedulers.mainThread()) // Run on the main thread by default
                .startWith(models.size() - 1) // Start loading without scroll event
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer visiblePosition) throws Exception {
                        return !isFetched()
                                && !isLoading()
                                && isIncreased(visiblePosition)
                                && doLoading(visiblePosition);
                    }
                })
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer visiblePosition) throws Exception {
                        return models.size(); // Offset
                    }
                })
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.DROP)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer offset) throws Exception {
                        setLoading(true, viewRef);
                    }
                })
                .observeOn(Schedulers.io()) // Do loading on the background thread
                .concatMap(new Function<Integer, Publisher<List<M>>>() {
                    @Override
                    public Publisher<List<M>> apply(@NonNull final Integer offset)
                            throws Exception {
                        return Flowable.just(loadNext(offset, getLimit()));
                    }
                })
                // .delay(3, TimeUnit.SECONDS)
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        return !isFetched();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // Observe results on the main thread
                .subscribeWith(new DisposableSubscriber<List<M>>() {
                                   @Override
                                   public void onNext(List<M> newModels) {
                                       setLoading(false, viewRef);
                                       if (newModels.size() > 0) {
                                           addNext(newModels, viewRef);
                                       }
                                       if (newModels.size() < getLimit()) {
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
     * @param newModels - next data, size > 0.
     * @param viewRef   - view that attached to this presenter
     */
    protected void addNext(List<M> newModels, WeakReference<V> viewRef) {
        int insertPosition = models.size();
        int insertCount = 0;
        for (M newModel : newModels) {
            boolean exist = false;
            for (M model : models) {
                if (model.getUuid().equals(newModel.getUuid())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                models.add(newModel);
                insertCount += 1;
            }
        }
        if (insertCount == 0) return;
        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
            viewRef.get().showInsert(insertPosition, insertCount);
        }
    }

    protected List<M> getModels() {
        return models;
    }

    @Nullable
    protected Integer getPosition(String uuid) {
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getUuid().equals(uuid)) return i;
        }
        return null;
    }

    private boolean isIncreased(int visiblePosition) {
        boolean increased = visiblePosition > previousPosition;
        previousPosition = visiblePosition;
        return increased;
    }

    protected void resetPreviousPosition() {
        previousPosition = -2;
    }

    /**
     * The decision whether to load a new pack of items.
     *
     * @param visiblePosition - current max visible position of items.
     * @return true if the load is needed.
     */
    protected boolean doLoading(int visiblePosition) {
        return models.size() <= ((visiblePosition + 1) * 2);
    }

    protected boolean isLoading() {
        return loading;
    }

    protected void setLoading(boolean loading) {
        this.loading = loading;
    }

    protected void setLoading(boolean loading, WeakReference<V> viewRef) {
        if (this.loading == loading) return;
        this.loading = loading;
        if (viewRef.get() != null && viewRef.get().isViewVisible()) {
            viewRef.get().showLoading(loading);
        }
    }

    protected boolean isFetched() {
        return fetched;
    }

    protected void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    protected void setFetched(boolean fetched, WeakReference<V> viewRef) {
        this.fetched = fetched;
        if (viewRef.get() != null) viewRef.get().setPagination(!fetched);
    }

    protected int getLimit() {
        return limit;
    }

    protected CompositeDisposable getComposite() {
        return composite;
    }
}
