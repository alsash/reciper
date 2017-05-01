package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alsash.reciper.mvp.presenter.StartPresenter;
import com.alsash.reciper.ui.application.ReciperApplication;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * An activity with startup splash set in styles.xml
 */
public class StartActivity extends AppCompatActivity {

    private static final long UI_DELAY_FULLSCREEN_MS = 1000; // PreLollipop only
    private static final long UI_DELAY_START_MS = 5000; // Not less than fullscreen delay
    @Inject
    StartPresenter presenter;
    private Runnable setFullscreenVisibility = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private Runnable startMainActivity = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(StartActivity.this, RecipeTabActivity.class));
        }
    };
    private Callable<Void> makeEntitiesIfNeed = new Callable<Void>() {
        @Override
        public Void call() throws Exception {

            return null;
        }
    };
    private Observable<Long> fullscreenVisibilityDelay;
    private Observable<Void> startupEntityIfNeedMaker;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ReciperApplication) getApplicationContext())
                .getAppComponent()
                .getStartComponentBuilder()
                .build()
                .inject(this);

        makeObservables();
        makeSubscriptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeSubscriptions(); // Return if has been already made
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearSubscriptions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSubscriptions();
        clearObservables();
    }

    private void makeObservables() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fullscreenVisibilityDelay = Observable
                    .timer(UI_DELAY_FULLSCREEN_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            fullscreenVisibilityDelay = Observable
                    .just(0L)
                    .observeOn(AndroidSchedulers.mainThread());
        }

        startupEntityIfNeedMaker = Observable
                .fromCallable(makeEntitiesIfNeed)
                .delaySubscription(UI_DELAY_START_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void makeSubscriptions() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) return;
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        compositeSubscription.add(fullscreenVisibilityDelay.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                setFullscreenVisibility.run();
            }
        }));
        compositeSubscription.add(startupEntityIfNeedMaker.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startMainActivity.run();
            }
        }));
    }

    private void clearSubscriptions() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription.clear();
            compositeSubscription = null;
        }
    }

    private void clearObservables() {
        fullscreenVisibilityDelay = null;
        startupEntityIfNeedMaker = null;
    }
}
