package com.alsash.reciper.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.alsash.reciper.ui.activity.RecipeCollectionsActivity;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.lang.ref.WeakReference;

import javax.inject.Singleton;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * A Navigator class for controlling transitions between application views
 */
@Singleton
public class AppNavigator {

    private WeakReference<Context> contextRef;
    private WeakReference<Context> activityContextRef;

    public AppNavigator(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    public AppNavigator fromActivity(Context activityContext) {
        this.activityContextRef = new WeakReference<>(activityContext);
        return this;
    }

    public void toRecipeCollectionsView() {
        Context context = getContext();
        if (context == null) return;
        Intent intent = new Intent(context, RecipeCollectionsActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void toRecipeDetailsView(long recipeId) {
        Toast.makeText(getContext(), "Recipe id = " + recipeId, Toast.LENGTH_SHORT).show();
    }

    public void toRecipeExpandView(long recipeId, FragmentManager fragmentManager) {
        RecipeBottomDialog.show(recipeId, fragmentManager);
    }

    @Nullable
    private Context getContext() {
        if (activityContextRef != null) {
            Context context = activityContextRef.get();
            activityContextRef = null;
            if (context != null) return context;
        }
        return this.contextRef.get();
    }
}
