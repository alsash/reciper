package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alsash.reciper.mvp.presenter.StartPresenter;
import com.alsash.reciper.mvp.view.StartView;
import com.alsash.reciper.ui.application.ReciperApplication;

import javax.inject.Inject;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * An activity with startup splash set in styles.xml
 */
public class StartActivity extends AppCompatActivity implements StartView {

    private static final boolean FULLSCREEN_DELAYED = SDK_INT < LOLLIPOP;
    private static final long FULLSCREEN_DELAY_MS = 1000;
    @Inject
    StartPresenter presenter;
    private Handler fullscreenHandler = FULLSCREEN_DELAYED ? new Handler() : null;
    private Runnable fullscreenSetter = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @Override
    public void setFullscreenVisibility() {
        if (!FULLSCREEN_DELAYED) fullscreenSetter.run(); // Else - implemented with Handler
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(StartActivity.this, RecipeTabActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ReciperApplication) getApplicationContext())
                .getAppComponent()
                .getStartComponentBuilder()
                .build()
                .inject(this);
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setForeground(true);
        if (FULLSCREEN_DELAYED) {
            fullscreenHandler.postDelayed(fullscreenSetter, FULLSCREEN_DELAY_MS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.setForeground(false);
        if (FULLSCREEN_DELAYED) fullscreenHandler.removeCallbacks(fullscreenSetter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.setView(null);
    }


}
