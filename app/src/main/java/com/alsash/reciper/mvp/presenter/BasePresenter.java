package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

/**
 * Base presenter
 */
public abstract class BasePresenter<V> {

    private WeakReference<V> viewRef;
    private boolean foreground;
    private boolean initialized;

    /**
     * Initialize presenter. Called when views is attached at first time
     */
    protected abstract void init();

    /**
     * Show data on view. Called when view is in foreground and presenter is initialized
     */
    protected abstract void show();

    /**
     * Clear presenter. Called when view is detached from presenter
     */
    protected abstract void clear();

    /**
     * Getter for view that has been set in setView {@link #setView(V)}
     *
     * @return view instance or null
     */
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
            // Attach view
            if (viewRef == null || viewRef.get() == null || !viewRef.get().equals(view)) {
                viewRef = new WeakReference<>(view);
                init();
            }
        } else {
            // Detach view
            if (viewRef != null) {
                viewRef.clear();
                viewRef = null;
                clear();
            }
        }
    }

    /**
     * Check if current view is in foreground
     *
     * @return - true if foreground was set
     */
    @UiThread
    public boolean isForeground() {
        return foreground;
    }

    /**
     * Set visibility of current view and show data if it is ready and view is in foreground
     *
     * @param foreground - true if view can show data
     */
    @UiThread
    public void setForeground(boolean foreground) {
        this.foreground = foreground;
        if (isInitialized() && isForeground()) show();
    }

    /**
     * Check if presentation data is initialized and can be shown
     *
     * @return - true if data can be shown
     */
    @UiThread
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * Set result of initialize and show data if view is in foreground
     *
     * @param initialized - true if data can be shown
     */
    @UiThread
    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
        if (isInitialized() && isForeground()) show();
    }
}