package com.alsash.reciper.mvp.presenter;

import android.os.Build;

import com.alsash.reciper.api.StorageApi;
import com.alsash.reciper.mvp.view.StartView;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Start view presenter
 */
public class StartPresenter extends BasePresenter<StartView> {

    private static final long UI_DELAY_FULLSCREEN_MS = 1000; // PreLollipop only
    private static final long UI_DELAY_START_MS = 5000; // Not less than fullscreen delay

    private final StorageApi storageApi;

    private CompositeSubscription compositeSubscription;

    private boolean fullscreenVisibilityInitialized;
    private boolean startMainActivityInitialized;

    private Callable<Void> makeEntitiesIfNeed = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            storageApi.getDatabaseApi().createStartupEntriesIfNeed();
            return null;
        }
    };

    public StartPresenter(StorageApi storageApi) {
        this.storageApi = storageApi;
    }

    @Override
    protected void init() {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            compositeSubscription.add(Observable
                    .timer(UI_DELAY_FULLSCREEN_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            fullscreenVisibilityInitialized = true;
                            setInitialized(true); // Call to show() if view is in foreground
                        }
                    })
            );
        } else {
            fullscreenVisibilityInitialized = true;
            setInitialized(true);
        }
        compositeSubscription.add(Observable
                .fromCallable(makeEntitiesIfNeed)
                .delaySubscription(UI_DELAY_START_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startMainActivityInitialized = true;
                        setInitialized(true);
                    }
                }));
    }

    @Override
    protected void show() {
        if (getView() == null) return;
        if (fullscreenVisibilityInitialized) getView().setFullscreenVisibility();
        if (startMainActivityInitialized) getView().startMainActivity();
    }

    @Override
    protected void clear() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription.clear();
            compositeSubscription = null;
        }
    }
}
