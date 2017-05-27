package com.alsash.reciper.mvp.view;

import android.support.annotation.StringRes;

/**
 * A Start View with fullscreen splash
 */
public interface StartView extends BaseView {

    void setFullscreenVisibility();

    void showNotification(@StringRes int id);

    void finishView();
}
