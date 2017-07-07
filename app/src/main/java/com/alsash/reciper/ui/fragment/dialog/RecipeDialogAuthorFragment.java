package com.alsash.reciper.ui.fragment.dialog;

import android.content.Intent;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.presenter.RecipeDialogAuthorPresenter;
import com.alsash.reciper.mvp.view.RecipeDialogAuthorView;

import javax.inject.Inject;

/**
 * Complex Category selection dialog
 */
public class RecipeDialogAuthorFragment
        extends BaseSelectionDialogFragment<Author, RecipeDialogAuthorView> {

    @Inject
    RecipeDialogAuthorPresenter presenter;
    @Inject
    NavigationLogic navigator;

    public static RecipeDialogAuthorFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDialogAuthorFragment(), intent);
    }

    @Override
    protected Class<?> getEntityClass() {
        return Author.class;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.fragment_recipe_dialog_author_title);
    }

    @Override
    protected Integer getLayoutRes() {
        return R.layout.fragment_recipe_dialog_author;
    }

    @Override
    protected RecipeDialogAuthorPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiDialogComponent()
                .inject(this);
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }
}