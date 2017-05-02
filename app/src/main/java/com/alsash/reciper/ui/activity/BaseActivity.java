package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alsash.reciper.mvp.presenter.BasePresenter;

/**
 * An root Activity with single BasePresenter
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BasePresenter presenter;

    /**
     * Called in {@link #onCreate(Bundle)} after super.onCreate() and before any other methods.
     *
     * @return BasePresenter inheritor for embedding in the activity life cycle
     */
    @Nullable
    protected abstract BasePresenter setupPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = setupPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) presenter.setInForeground(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) presenter.setInForeground(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.setView(null);
            presenter = null;
        }
    }
}
