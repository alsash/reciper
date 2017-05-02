package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.view.StartView;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Start view presenter
 */
public class StartPresenter extends BasePresenter<StartView> {

    private static final long START_DELAY_MS = 5000; // For debug

    private final StorageApi storageApi;

    private Callable<Void> startInBackground = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            storageApi.getDatabaseApi().createStartupEntriesIfNeed();
            return null;
        }
    };
    private Subscription startSubscription;

    public StartPresenter(StorageApi storageApi) {
        this.storageApi = storageApi;
    }

    @Override
    protected void init() {
        if (getView() != null) getView().setFullscreenVisibility(); // No need to wait background
        if (startSubscription == null && !isInitialized()) {        // Run startInBackground
            startSubscription = Observable
                    .fromCallable(startInBackground)
                    .subscribeOn(Schedulers.io())
                    .delay(START_DELAY_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            setInitialized(true);                   // Call to show() in foreground
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
