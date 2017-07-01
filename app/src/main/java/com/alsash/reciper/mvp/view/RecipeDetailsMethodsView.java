package com.alsash.reciper.mvp.view;

import com.alsash.reciper.app.util.MutableBoolean;
import com.alsash.reciper.mvp.model.entity.Method;

import java.util.List;

/**
 * Simple view for its presenter
 */
public interface RecipeDetailsMethodsView extends BaseView {

    void showMethods(List<Method> methods);

    void showMethodsTitle(int methodsSize);

    void showMethodAdd(int position);

    void showMethodDelete(int position, MutableBoolean reject);

    void showMethodMove(int fromPosition, int toPosition);

}
