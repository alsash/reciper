package com.alsash.reciper.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.restriction.EntityRestriction;
import com.alsash.reciper.ui.activity.RecipeCollectionsActivity;
import com.alsash.reciper.ui.activity.SingleFragmentActivity;
import com.alsash.reciper.ui.fragment.RecipeCollectionCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionGridFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionLabelFragment;
import com.alsash.reciper.ui.fragment.RecipeRestrictListFragment;

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
    private boolean contextIsActivity;

    public AppNavigator(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    public AppNavigator fromActivity(Context activityContext) {
        this.activityContextRef = new WeakReference<>(activityContext);
        this.contextIsActivity = true;
        return this;
    }

    public void toRecipeCollectionsView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.RECIPE_ID, "",
                        obtainFlags(new Intent(context, RecipeCollectionsActivity.class)))
        );
    }

    public void toRecipeListView(Category category) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.CATEGORY_ID, category.getUuid(),
                        obtainFlags(new Intent(context, SingleFragmentActivity.class)))
        );
    }

    public void toRecipeListView(Label label) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.LABEL_ID, label.getUuid(),
                        obtainFlags(new Intent(context, SingleFragmentActivity.class)))
        );
    }

    public Fragment[] getFragmentCollections(Intent intent) {
        if (intent.getStringExtra(AppContract.Key.RECIPE_ID) != null)
            return new Fragment[]{
                    RecipeCollectionCategoryFragment.newInstance(),
                    RecipeCollectionGridFragment.newInstance(),
                    RecipeCollectionLabelFragment.newInstance()};
        return null;
    }

    public Fragment getFragmentSingle(Intent intent) {
        if (intent.getStringExtra(AppContract.Key.CATEGORY_ID) != null
                || intent.getStringExtra(AppContract.Key.LABEL_ID) != null) {
            return RecipeRestrictListFragment.newInstance(intent);
        }
        return null;
    }

    public EntityRestriction getRestriction(Intent intent) {
        if (intent.getStringExtra(AppContract.Key.CATEGORY_ID) != null) {
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.CATEGORY_ID),
                    Category.class
            );
        } else if (intent.getStringExtra(AppContract.Key.LABEL_ID) != null) {
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.LABEL_ID),
                    Label.class
            );
        }
        return null;
    }


    public void toRecipeDetailsView(Recipe recipe) {
        Toast.makeText(getContext(), "Recipe id = " + recipe.getId(), Toast.LENGTH_SHORT).show();
    }

    public void toRecipeAddView() {
        // Do nothing...
    }

    private Intent obtainRestriction(String restrictionName,
                                     String restrictionValue,
                                     Intent intent) {
        intent.putExtra(restrictionName, restrictionValue);
        return intent;
    }

    private Intent obtainFlags(Intent intent) {
        if (!contextIsActivity) {
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
            contextIsActivity = false;
        }
        return intent;
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
