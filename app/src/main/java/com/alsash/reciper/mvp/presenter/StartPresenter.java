package com.alsash.reciper.mvp.presenter;

import android.util.Log;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.view.StartView;

import java.lang.ref.WeakReference;

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

    private static final String TAG = "StartPresenter";

    private final StorageLogic storage;
    private final CompositeDisposable composite = new CompositeDisposable();
    private boolean fetched;

    public StartPresenter(StorageLogic storage) {
        this.storage = storage;
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
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
                }));
    }
}
