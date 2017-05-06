package com.alsash.reciper.mvp.presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseView;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

/**
 * A Presenter that manage RecipeListInteractions
 */

public abstract class BaseRecipeListPresenter<V extends BaseView> implements BasePresenter<V> {

    public void onRecipeExpand(FragmentManager fragmentManager, Recipe recipe) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe.getId());
        bottomDialog.show(fragmentManager, bottomDialog.getTag());
    }

    public void onRecipeOpen(Context context, Recipe recipe) {
        // Go to RecipeDetails
    }
}
