package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

/**
 * The base presenter of any IView's instance
 */
public abstract class BaseWeakPresenter<V> {

    private WeakReference<V> viewRef;
    private boolean inForeground;
    private boolean initialized;

    /**
     * Initialize the new view.
     * Called when the view is attached to this presenter.
     */
    protected abstract void init();

    /**
     * Show data on the view.
     * Called when the view {@link #isInForeground()}
     * and this presenter {@link #isInitialized()}.
     * Any of that's two events can call this method.
     */
    protected abstract void show();

    /**
     * Clear previously attached view.
     * Called when the view is detached from  this presenter.
     */
    protected abstract void clear();

    /**
     * Getter for the view that attached by {@link #setView(V)}
     *
     * @return the view's instance or null
     */
    @UiThread
    @Nullable
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * Attach or detach view to presenter.
     *
     * @param view is the concrete view (BaseActivity, Fragment, etc.),
     *             that implement view's interface.
     *             If not null, the view will be attached to the presenter,
     *             otherwise, the view will be detached.
     */
    @UiThread
    public void setView(@Nullable V view) {
        if (view != null) {
            // Attach view
            viewRef = new WeakReference<>(view);
            init();
        } else {
            // Detach view
            clear();
            if (viewRef != null) {
                viewRef.clear();
                viewRef = null;
            }
        }
    }

    /**
     * Check if current view is in inForeground
     *
     * @return true if view can show data
     */
    @UiThread
    public boolean isInForeground() {
        return inForeground;
    }

    /**
     * Set visibility of the current view.
     * After set, will be called to {@link #show()} if this presenter {@link #isInitialized()}
     * and attached view is {@link #isInForeground()}
     *
     * @param inForeground - true if view can show data
     */
    @UiThread
    public void setInForeground(boolean inForeground) {
        this.inForeground = inForeground;
        if (isInitialized() && isInForeground()) show();
    }

    /**
     * Check if presentation data is initialized in {@link #init()}
     * and {@link #setInitialized(boolean)} has been called and can be shown
     *
     * @return true if data can be shown
     */
    @UiThread
    protected boolean isInitialized() {
        return initialized;
    }

    /**
     * Set result of initialize and show data if view is in inForeground
     * After set, will be called to {@link #show()} if this presenter {@link #isInitialized()}
     * and attached view is {@link #isInForeground()}
     *
     * @param initialized - true if data can be shown
     */
    @UiThread
    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
        if (isInitialized() && isInForeground()) show();
    }
}