package com.alsash.reciper.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.view.BaseView;

/**
 * An injection-ready BaseActivity with single presenter
 */
public abstract class BaseActivity<V extends BaseView> extends AppCompatActivity
        implements BaseView {

    private BasePresenter<V> presenter;
    private boolean visible;

    /**
     * Called in {@link #onCreate(Bundle)} before super.onCreate() and any other methods.
     *
     * @return presenter instance for embedding in the activity life cycle
     */
    protected abstract BasePresenter<V> inject();

    @Override
    public synchronized boolean isViewVisible() {
        return visible;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = inject();
        presenter.attach(getThisView());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        visible = true;
        presenter.visible(getThisView());
    }

    @Override
    protected void onStop() {
        visible = false;
        presenter.invisible(getThisView());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
        presenter = null;
    }

    @SuppressWarnings("unchecked")
    protected V getThisView() {
        return (V) this;
    }

    protected Context getThisContext() {
        return this;
    }
}
