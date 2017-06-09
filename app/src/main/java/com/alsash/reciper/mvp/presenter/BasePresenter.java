package com.alsash.reciper.mvp.presenter;

import android.support.annotation.UiThread;

import com.alsash.reciper.mvp.view.BaseView;

/**
 * A BasePresenter's interface, which is embedded into the view life cycle.
 * Responsibility for the order of embedding into the view belong to the view itself.
 */
public interface BasePresenter<V extends BaseView> {

    @UiThread
    void attach(V view);

    @UiThread
    void visible(V view);

    @UiThread
    void invisible(V view);

    @UiThread
    void refresh(V view);

    @UiThread
    void detach();
}
