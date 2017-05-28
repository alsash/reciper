package com.alsash.reciper.mvp.presenter;

import android.util.Log;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.exception.NoInternetException;
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

    private final StorageLogic storage;
    private final AppNavigator navigator;
    private final CompositeDisposable composite = new CompositeDisposable();
    private boolean fetched;

    public StartPresenter(StorageLogic storage, AppNavigator navigator) {
        this.storage = storage;
        this.navigator = navigator;
    }

    @Override
    public void attach(StartView view) {
        view.setFullscreenVisibility();
        if (!fetched) fetch(new WeakReference<>(view));
    }

    @Override
    public void visible(StartView view) {
        if (!fetched) return;
        view.finishView();
        detach();
        navigator.toMainView();
    }

    @Override
    public void invisible(StartView view) {
        // Do nothing
    }

    @Override
    public void refresh(StartView view) {
        detach();
        fetched = false;
        attach(view);
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
                        storage.createStartupEntitiesIfNeed();
                    }
                })
                .delay(START_DELAY_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        // This method called on Main Thread, so access is thread-safe
                        fetched = true;
                        if (viewRef.get() == null) return;
                        viewRef.get().showNotification(R.string.notification_recipes_download_ok);
                        if (viewRef.get().isViewVisible()) visible(viewRef.get());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof NoInternetException) {
                            if (viewRef.get() != null) {
                                viewRef.get().showNotification(
                                        R.string.notification_recipes_download_err_no_internet);
                            }
                        } else {
                            Log.e(TAG, e.getMessage(), e);
                        }
                        onComplete();
                    }
                }));
    }
}
