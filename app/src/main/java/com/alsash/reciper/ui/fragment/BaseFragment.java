package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.mvp.presenter.BasePresenter;

/**
 * An root Fragment with single BasePresenter
 */
public abstract class BaseFragment extends Fragment {

    private BasePresenter presenter;

    /**
     * Called in {@link #onAttach(Context)} before super.onAttach() and any other methods.
     *
     * @return BasePresenter inheritor for embedding in the activity life cycle
     */
    @Nullable
    protected abstract BasePresenter setupPresenter();

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
