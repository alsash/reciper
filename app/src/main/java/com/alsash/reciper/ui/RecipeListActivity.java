package com.alsash.reciper.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.RecipeListAdapter;
import com.alsash.reciper.ui.dialog.RecipeBottomDialog;

public class RecipeListActivity extends DrawerFrameActivity implements RecipeListAdapter.OnRecipeInteraction {

    private FloatingActionButton mainFab;

    @Override
    public void click(Recipe recipe) {

    }

    @Override
    public void expand(Recipe recipe) {
        RecipeBottomDialog recipeBottomDialog = RecipeBottomDialog.newInstance(recipe);
        recipeBottomDialog.show(getSupportFragmentManager(), recipeBottomDialog.getTag());
    }

    @Override
    protected Fragment getFrameFragment(@Nullable Intent activityIntent) {
        return RecipeListFragment.newInstance();
    }

    @Override
    protected void setupFab(FloatingActionButton fab) {
        this.mainFab = fab;
    }

}
