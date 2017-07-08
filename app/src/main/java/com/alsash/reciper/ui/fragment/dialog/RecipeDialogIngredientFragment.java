package com.alsash.reciper.ui.fragment.dialog;

import android.content.Intent;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.presenter.RecipeDialogIngredientPresenter;
import com.alsash.reciper.mvp.view.RecipeDialogIngredientView;

import javax.inject.Inject;

/**
 * Complex Food selection dialog
 */
public class RecipeDialogIngredientFragment
        extends BaseSelectionDialogFragment<Food, RecipeDialogIngredientView> {

    @Inject
    RecipeDialogIngredientPresenter presenter;
    @Inject
    NavigationLogic navigator;

    public static RecipeDialogIngredientFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDialogIngredientFragment(), intent);
    }

    @Override
    protected Class<?> getEntityClass() {
        return Food.class;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.fragment_recipe_dialog_food_title);
    }

    @Override
    protected Integer getLayoutRes() {
        return R.layout.fragment_recipe_dialog_food;
    }

    @Override
    protected RecipeDialogIngredientPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiDialogComponent()
                .inject(this);
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }
}
