package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.view.StartView;

import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Start view presenter
 */
public class StartPresenter extends BasePresenter<StartView> {

    private static final long START_DELAY_MS = 1000; // For debug

    private final StorageApi storageApi;

    private Subscription startSubscription;

    public StartPresenter(StorageApi storageApi) {
        this.storageApi = storageApi;
    }

    @Override
    protected void init() {
        if (getView() != null) getView().setFullscreenVisibility(); // No need to wait background
        if (startSubscription == null && !isInitialized()) {        // Run in background
            startSubscription = Completable
                    .fromAction(new Action0() {
                        @Override
                        public void call() {
                            storageApi.getDatabaseApi().createStartupEntriesIfNeed();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .delay(START_DELAY_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action0() {
                        @Override
                        public void call() {
                            setInitialized(true);                   // Call to show() in foreground
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            // Do nothing
                        }
                    });
        }
    }

    @Override
    protected void show() {
        if (getView() != null) getView().startMainActivity();
    }

    @Override
    protected void clear() {
        if (startSubscription != null) {
            startSubscription.unsubscribe();
            startSubscription = null;
        }
    }
}
