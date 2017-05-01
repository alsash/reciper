package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

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

    private static final long UI_DELAY_START_MS = 5000; // Not less than fullscreen delay

    private final StorageApi storageApi;

    private Callable<Void> makeEntitiesIfNeed = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            storageApi.getDatabaseApi().createStartupEntriesIfNeed();
            return null;
        }
    };
    private Subscription startMainActivitySubscription;

    public StartPresenter(StorageApi storageApi) {
        this.storageApi = storageApi;
    }

    @Override
    public void setView(@Nullable StartView view) {
        super.setView(view);
        if (view != null) view.setFullscreenVisibility();
    }

    @Override
    protected void init() {
        if (startMainActivitySubscription == null) {
            startMainActivitySubscription = Observable
                    .fromCallable(makeEntitiesIfNeed)
                    .subscribeOn(Schedulers.io())
                    .delay(UI_DELAY_START_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            setInitialized(true); // Call to show() if view in foreground
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
        if (startMainActivitySubscription != null) {
            startMainActivitySubscription.unsubscribe();
            startMainActivitySubscription = null;
        }
    }
}
