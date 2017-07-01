package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;

import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.StartPresenter;
import com.alsash.reciper.mvp.view.StartView;

import javax.inject.Inject;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * An activity with startup splash that set in styles
 */
public class StartActivity extends BaseActivity<StartView> implements StartView {

    private static final boolean FULLSCREEN_DELAYED = SDK_INT < LOLLIPOP;
    private static final long FULLSCREEN_DELAY_MS = 100;

    private final Handler fullscreenHandler = FULLSCREEN_DELAYED ? new Handler() : null;
    private final Runnable fullscreenSetter = new Runnable() {
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

    @Inject
    StartPresenter presenter;
    @Inject
    NavigationLogic navigator;

    @Override
    protected BasePresenter<StartView> inject() {
        ((ReciperApp) getApplicationContext())
                .getUiStartComponent()
                .inject(this);
        return presenter;
    }

    @Override
    public void setFullscreenVisibility() {
        if (!FULLSCREEN_DELAYED) fullscreenSetter.run(); // Else - implemented with Handler
    }

    @Override
    public void finishView() {
        navigator.fromActivity(getThisContext()).toRecipesView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FULLSCREEN_DELAYED) {
            fullscreenHandler.postDelayed(fullscreenSetter, FULLSCREEN_DELAY_MS);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (FULLSCREEN_DELAYED) fullscreenHandler.removeCallbacks(fullscreenSetter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
