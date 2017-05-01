package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alsash.reciper.mvp.presenter.StartPresenter;
import com.alsash.reciper.mvp.view.StartView;
import com.alsash.reciper.ui.application.ReciperApplication;

import javax.inject.Inject;

/**
 * An activity with startup splash set in styles.xml
 */
public class StartActivity extends AppCompatActivity implements StartView {

    @Inject
    StartPresenter presenter;

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.setForeground(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.setView(null);
    }

    @SuppressLint("InlinedApi")
    @Override
    public void setFullscreenVisibility() {
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(StartActivity.this, RecipeTabActivity.class));
    }
}
