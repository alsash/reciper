package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.BaseWeakPresenter;
import com.alsash.reciper.mvp.view.BaseView;

/**
 * An root Fragment with single BaseWeakPresenter
 */
public abstract class BaseFragment<V extends BaseView> extends Fragment implements BaseView {

    private BaseWeakPresenter presenter;
    private boolean visible;

    /**
     * Called in {@link #onCreate(Bundle)} before super.onCreate() and any other methods.
     *
     * @return presenter instance for embedding in the activity life cycle
     */
    protected abstract BasePresenter<V> inject();

    @Override
    public void onAttach(Context context) {
        presenter = setupPresenter();
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.setInForeground(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) presenter.setInForeground(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter.setView(null);
            presenter = null;
        }
        super.onDetach();
    }
}
