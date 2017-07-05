package com.alsash.reciper.ui.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.view.BaseView;

/**
 * Complex creation dialog
 */
public abstract class BaseDialogFragment<V extends BaseView> extends AppCompatDialogFragment
        implements BaseView {

    protected final MutableBoolean visible = new MutableBoolean();
    private BasePresenter<V> presenter;

    /**
     * Called in {@link #onAttach(Context)} before super.onAttach() and any other methods.
     *
     * @return presenter instance for embedding in the fragment life cycle
     */
    protected abstract BasePresenter<V> inject();

    protected abstract void clickPositive();

    protected abstract void clickNegative();

    @Nullable
    protected String getTitle() {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = getTitle();
        if (title != null) builder.setTitle(title);
        builder.setView(onCreateView(LayoutInflater.from(getActivity()), null, null))
                .setPositiveButton(android.R.string.ok, null) // prevent dismiss
                .setNegativeButton(android.R.string.cancel, null); // prevent dismiss
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPositive();
                    }
                });
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickNegative();
                    }
                });
            }
        });
        return dialog;
    }

    @Override
    public boolean isViewVisible() {
        return visible.is();
    }

    @Override
    public void onAttach(Context context) {
        visible.set(false);
        presenter = inject();
        if (presenter != null) presenter.attach(getThisView());
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        visible.set(true);
        if (presenter != null) presenter.visible(getThisView());
    }

    @Override
    public void onStop() {
        visible.set(false);
        if (presenter != null) presenter.invisible(getThisView());
        super.onStop();
    }

    @Override
    public void onDetach() {
        visible.set(false);
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
        super.onDetach();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        visible.set(false);
        if (presenter != null) {
            presenter.invisible(getThisView());
            presenter.detach();
            presenter = null;
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        visible.set(false);
        if (presenter != null) {
            presenter.invisible(getThisView());
            presenter.detach();
        }
        super.onCancel(dialog);
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
