package com.alsash.reciper.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.alsash.reciper.ui.activity.RecipeTabActivity;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import javax.inject.Singleton;

/**
 * A Navigator class for controlling transitions between application views
 */
@Singleton
public class AppNavigator {

    private final Context context;

    public AppNavigator(Context context) {
        this.context = context;
    }

    public void toMainView() {
        context.startActivity(new Intent(context, RecipeTabActivity.class));
    }

    public void toRecipeMainView(long recipeId) {
        Toast.makeText(context, "Recipe id = " + recipeId, Toast.LENGTH_LONG).show();
    }

    public void toRecipeExpandView(long recipeId, FragmentManager fragmentManager) {
        RecipeBottomDialog.show(recipeId, fragmentManager);
    }
}
