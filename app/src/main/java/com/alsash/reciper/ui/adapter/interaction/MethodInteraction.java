package com.alsash.reciper.ui.adapter.interaction;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.mvp.model.entity.Method;

/**
 * A recipe method interactions
 */
public interface MethodInteraction {

    void onMethodEdit(Method method, String newBody);

    void onMethodDrag(RecyclerView.ViewHolder holder);
}
