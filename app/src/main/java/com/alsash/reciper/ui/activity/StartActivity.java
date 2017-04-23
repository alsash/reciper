package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alsash.reciper.database.ApiDatabase;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * An activity with startup splash set in styles.xml
 */
public class StartActivity extends AppCompatActivity {

    private static final long UI_DELAY_FULLSCREEN_MS = 100; // PreLollipop only
    private static final long UI_DELAY_START_MS = 100;

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
            ApiDatabase.getInstance().createStartupEntriesIfNeed(StartActivity.this);
            return null;
        }
    };

    private Observable<Long> fullscreenVisibilityDelay;
    private Observable<Void> startupEntityIfNeedMaker;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeObservables();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeSubscriptions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearSubscriptions(false); // Set compositeSubscription to null
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearObservables();
    }

    private void makeObservables() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fullscreenVisibilityDelay = Observable
                    .fromCallable(new Callable<Long>() {
                        @Override
                        public Long call() throws Exception {
                            return 0L;
                        }
                    })
                    .observeOn(Schedulers.computation())
                    .subscribeOn(AndroidSchedulers.mainThread());
        } else {
            fullscreenVisibilityDelay = Observable
                    .timer(UI_DELAY_FULLSCREEN_MS, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread());
        }

        startupEntityIfNeedMaker = Observable
                .fromCallable(makeEntitiesIfNeed)
                .observeOn(Schedulers.io())
                .delaySubscription(UI_DELAY_START_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    private void makeSubscriptions() {
        clearSubscriptions(true); // Recreate compositeSubscription
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

    private void clearSubscriptions(boolean recreate) {
        if (compositeSubscription != null) compositeSubscription.unsubscribe();
        if (recreate) {
            compositeSubscription = new CompositeSubscription(); // Enable adding new Subscriptions
        } else {
            compositeSubscription = null;
        }
    }

    private void clearObservables() {
        fullscreenVisibilityDelay = null;
        startupEntityIfNeedMaker = null;
    }
}
