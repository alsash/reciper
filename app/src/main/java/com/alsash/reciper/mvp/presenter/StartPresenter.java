package com.alsash.reciper.mvp.presenter;

import android.util.Log;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.mvp.view.StartView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Start view presenter
 */
public class StartPresenter implements BasePresenter<StartView> {

    private static final long START_DELAY_MS = 1000; // For debug
    private static final String TAG = "StartPresenter";

    private final StorageApi storageApi;
    private final AppNavigator navigator;
    private final CompositeDisposable composite = new CompositeDisposable();
    private boolean fetched;
    private boolean started;

    public StartPresenter(StorageApi storageApi, AppNavigator navigator) {
        this.storageApi = storageApi;
        this.navigator = navigator;
    }

    @Override
    public void attach(StartView view) {
        view.setFullscreenVisibility();
        if (!fetched) fetch(new WeakReference<>(view));
    }

    @Override
    public void visible(StartView view) {
        if (!isFetched() || isStarted()) return;
        setStarted(true);
        view.finishView();
        detach();
        navigator.toMainView();
    }

    @Override
    public void invisible(StartView view) {
        setStarted(false);
    }

    @Override
    public void detach() {
        composite.dispose(); // set Observers to null, so they are not holds any shadow references
        composite.clear();   // in v.2.1.0 - same as dispose(), but without set isDispose()
    }

    private void fetch(final WeakReference<StartView> viewRef) {
        composite.add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageApi.createStartupEntriesIfNeed();
                    }
                })
                .delay(START_DELAY_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        // This method called on Main Thread, so access is thread-safe
                        setFetched(true);
                        if (viewRef.get() == null) return;
                        if (viewRef.get().isViewVisible()) visible(viewRef.get());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }));
    }

    protected boolean isFetched() {
        return fetched;
    }

    protected void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    protected boolean isStarted() {
        return started;
    }

    protected void setStarted(boolean started) {
        this.started = started;
    }
}
