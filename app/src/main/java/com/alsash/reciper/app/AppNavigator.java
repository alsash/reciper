package com.alsash.reciper.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.alsash.reciper.ui.activity.RecipeCollectionsActivity;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import javax.inject.Singleton;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * A Navigator class for controlling transitions between application views
 */
@Singleton
public class AppNavigator {

    private final Context context;
    private Context activityContext;

    public AppNavigator(Context context) {
        this.context = context;
    }

    public AppNavigator fromActivity(Context activityContext) {
        this.activityContext = activityContext;
        return this;
    }

    public void toRecipeCollectionsView() {
        Context context = getContext();
        Intent intent = new Intent(context, RecipeCollectionsActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void toRecipeDetailsView(long recipeId) {
        Toast.makeText(context, "Recipe id = " + recipeId, Toast.LENGTH_LONG).show();
    }

    public void toRecipeExpandView(long recipeId, FragmentManager fragmentManager) {
        RecipeBottomDialog.show(recipeId, fragmentManager);
    }

    private Context getContext() {
        if (activityContext != null) {
            Context context = activityContext;
            activityContext = null;
            return context;
        }
        return this.context;
    }
}
