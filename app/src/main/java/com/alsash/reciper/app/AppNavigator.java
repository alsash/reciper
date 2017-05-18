package com.alsash.reciper.app;

import android.content.Context;
import android.content.Intent;

import com.alsash.reciper.ui.activity.RecipeTabActivity;

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

    public void toRecipeView(long recipeId) {

    }
}
