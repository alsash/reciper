package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.view.BaseView;

/**
 * An injection-ready BaseFragment with single presenter
 */
public abstract class BaseFragment<V extends BaseView> extends Fragment implements BaseView {

    private BasePresenter<V> presenter;
    private boolean visible;

    protected static <F extends Fragment> F getThisFragment(F fragment, Intent intent) {
        Bundle args = new Bundle();
        args.putParcelable(Intent.EXTRA_INTENT, intent);
        fragment.setArguments(args);
        return fragment;
    }

    protected static Intent getThisIntent(Fragment fragment) {
        return (Intent) fragment.getArguments().getParcelable(Intent.EXTRA_INTENT);
    }

    /**
     * Called in {@link #onAttach(Context)} before super.onAttach() and any other methods.
     *
     * @return presenter instance for embedding in the fragment life cycle
     */
    protected abstract BasePresenter<V> inject();

    @Override
    public synchronized boolean isViewVisible() {
        return visible;
    }

    @Override
    public void onAttach(Context context) {
        presenter = inject();
        presenter.attach(getThisView());
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        visible = true;
        presenter.visible(getThisView());
    }

    @Override
    public void onStop() {
        visible = false;
        presenter.invisible(getThisView());
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detach();
        presenter = null;
    }

    @SuppressWarnings("unchecked")
    protected <P> P getThisPresenter() {
        return (P) presenter;
    }

    @SuppressWarnings("unchecked")
    protected V getThisView() {
        return (V) this;
    }
}
