package com.alsash.reciper.mvp.presenter;

import android.support.annotation.NonNull;

import com.alsash.reciper.mvp.view.BaseView;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An abstract Composite presenter that implement Composite pattern
 */
public abstract class CompositePresenter<V extends BaseView> implements BasePresenter<V> {

    private final Set<BasePresenter<V>> presenters = new LinkedHashSet<>();

    @Override
    public void attach(V view) {
        for (BasePresenter<V> presenter : presenters) {
            presenter.attach(view);
        }
    }

    @Override
    public void visible(V view) {
        for (BasePresenter<V> presenter : presenters) {
            presenter.visible(view);
        }
    }

    @Override
    public void invisible(V view) {
        for (BasePresenter<V> presenter : presenters) {
            presenter.invisible(view);
        }
    }

    @Override
    public void detach() {
        for (BasePresenter<V> presenter : presenters) {
            presenter.detach();
        }
    }

    @NonNull
    public Set<BasePresenter<V>> getPresenters() {
        return presenters;
    }
}
