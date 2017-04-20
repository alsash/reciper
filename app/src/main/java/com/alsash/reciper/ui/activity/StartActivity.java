package com.alsash.reciper.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alsash.reciper.database.ApiDatabase;

public class StartActivity extends AppCompatActivity {

    // private static final long UI_DELAY_FULLSCREEN_MS = 100;
    // private static final long UI_DELAY_START_MS = 2000;

    private Handler handler = new Handler();

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
            startActivity(new Intent(StartActivity.this, RecipeListActivity.class));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) return;
        setFullscreenVisibility.run();
        ApiDatabase.getInstance().createStartupEntriesIfNeed(this, startMainActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   handler.postDelayed(setFullscreenVisibility, UI_DELAY_FULLSCREEN_MS);
        //   handler.postDelayed(startMainActivity, UI_DELAY_START_MS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  handler.removeCallbacks(setFullscreenVisibility);
        //  handler.removeCallbacks(startMainActivity);
    }
}
