package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Method;

/**
 * A recipe method interactions
 */
public interface MethodInteraction {

    void onMethodEdit(Method method, String newBody);
}
