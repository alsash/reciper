package com.alsash.reciper.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

/**
 * Base presenter
 */
public abstract class BasePresenter<V> {

    private WeakReference<V> viewRef;

    public abstract void initView();

    public abstract void completeView();

    @UiThread
    @Nullable
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * Attach or detach view from presenter
     *
     * @param view concrete view (Activity, Fragment, etc.),
     *             if not null, the view will be attached to the presenter
     *             Otherwise, the view will be detached.
     */
    @UiThread
    public void setView(@Nullable V view) {
        if (view != null) {
            attachView(view);
        } else {
            detachView();
        }
    }

    private void attachView(@NonNull V view) {
        // Attach only if view has not been already attached
        if (viewRef == null || viewRef.get() == null || !viewRef.get().equals(view)) {
            viewRef = new WeakReference<>(view);
        }
    }

    private void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }
}