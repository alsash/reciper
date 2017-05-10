package com.alsash.reciper.mvp.presenter;

import android.util.Log;

import com.alsash.reciper.api.StorageApi;
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

    private final CompositeDisposable composite = new CompositeDisposable();
    private final StorageApi storageApi;
    private boolean fetched;
    private boolean started;

    public StartPresenter(StorageApi storageApi) {
        this.storageApi = storageApi;
    }

    @Override
    public void attach(StartView view) {
        view.setFullscreenVisibility();
        if (!fetched) fetch(view);
    }

    @Override
    public void visible(StartView view) {
        if (!fetched || started) return;
        started = true;
        view.startMainActivity();
    }

    @Override
    public void invisible(StartView view) {
        started = false;
    }

    @Override
    public void detach() {
        if (composite.isDisposed()) return;
        composite.dispose(); // set Observers to null, so they are not holds any shadow references
        composite.clear();   // in v.2.1.0 - same as dispose(), but without set isDispose()
    }

    private void fetch(StartView view) {
        composite.add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageApi.createStartupEntriesIfNeed();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .delay(START_DELAY_MS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    private WeakReference<StartView> viewRef;

                    @Override
                    public void onComplete() {
                        // This method called on Main Thread, so access is thread-safe
                        fetched = true;
                        if (viewRef.get() == null) return;
                        if (viewRef.get().isViewVisible()) visible(viewRef.get());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                        onComplete();
                    }

                    public DisposableCompletableObserver setView(StartView v) {
                        viewRef = new WeakReference<>(v);
                        return this;
                    }
                }.setView(view)));
    }
}
