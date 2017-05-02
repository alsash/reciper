package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.StartPresenter;
import com.alsash.reciper.mvp.view.StartView;
import com.alsash.reciper.ui.application.ReciperApplication;

import javax.inject.Inject;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * An activity with startup splash set in styles.xml
 */
public class StartActivity extends BaseActivity implements StartView {

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
    protected BasePresenter setupPresenter() {
        ((ReciperApplication) getApplicationContext())
                .getAppComponent()
                .getStartComponentBuilder()
                .build()
                .inject(this);
        presenter.setView(this);
        return presenter; // Presenter will be embedded in activity life cycle
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FULLSCREEN_DELAYED) {
            fullscreenHandler.postDelayed(fullscreenSetter, FULLSCREEN_DELAY_MS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (FULLSCREEN_DELAYED) fullscreenHandler.removeCallbacks(fullscreenSetter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (FULLSCREEN_DELAYED) fullscreenHandler = null;
    }
}
