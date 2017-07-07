package com.alsash.reciper.ui.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.presenter.RecipeDialogLabelPresenter;
import com.alsash.reciper.mvp.view.RecipeDialogLabelView;

import javax.inject.Inject;

/**
 * Complex Category selection dialog
 */
public class RecipeDialogLabelFragment
        extends BaseSelectionDialogFragment<Label, RecipeDialogLabelView> {

    @Inject
    RecipeDialogLabelPresenter presenter;
    @Inject
    NavigationLogic navigator;

    public static RecipeDialogLabelFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDialogLabelFragment(), intent);
    }

    @Override
    protected Class<?> getEntityClass() {
        return Label.class;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.fragment_recipe_dialog_label_title);
    }

    @Override
    protected Integer getLayoutRes() {
        return R.layout.fragment_recipe_dialog_label;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup group,
                             @Nullable Bundle state) {
        View layout = super.onCreateView(inflater, group, state);
        list.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) list.getLayoutManager();
        lm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        list.setLayoutManager(lm);
        return layout;
    }

    @Override
    protected RecipeDialogLabelPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiDialogComponent()
                .inject(this);
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }
}
