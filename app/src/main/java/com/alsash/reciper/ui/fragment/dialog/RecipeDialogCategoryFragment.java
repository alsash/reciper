package com.alsash.reciper.ui.fragment.dialog;

import android.content.Intent;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.presenter.RecipeDialogCategoryPresenter;
import com.alsash.reciper.mvp.view.RecipeDialogCategoryView;

import javax.inject.Inject;

/**
 * Complex Category selection dialog
 */
public class RecipeDialogCategoryFragment
        extends BaseSelectionDialogFragment<Category, RecipeDialogCategoryView> {

    @Inject
    RecipeDialogCategoryPresenter presenter;
    @Inject
    NavigationLogic navigator;

    public static RecipeDialogCategoryFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDialogCategoryFragment(), intent);
    }

    @Override
    protected Class<?> getEntityClass() {
        return Category.class;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.fragment_recipe_dialog_category_title);
    }

    @Override
    protected Integer getLayoutRes() {
        return R.layout.fragment_recipe_dialog_category;
    }

    @Override
    protected RecipeDialogCategoryPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiDialogComponent()
                .inject(this);
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }
}
