package com.alsash.reciper.logic;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.ui.activity.RecipeCollectionsActivity;
import com.alsash.reciper.ui.activity.RecipeDetailsActivity;
import com.alsash.reciper.ui.activity.SingleFragmentActivity;
import com.alsash.reciper.ui.fragment.RecipeCollectionCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionGridFragment;
import com.alsash.reciper.ui.fragment.RecipeCollectionLabelFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsDescriptionFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsIngredientsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsMethodsFragment;
import com.alsash.reciper.ui.fragment.RecipeRestrictListFragment;

import java.lang.ref.WeakReference;

import javax.inject.Singleton;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * A Navigation Logic for controlling transitions between application views
 */
@Singleton
public class NavigationLogic {

    private WeakReference<Context> contextRef;
    private WeakReference<Context> activityContextRef;
    private boolean contextIsActivity;

    public NavigationLogic(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    public NavigationLogic fromActivity(Context activityContext) {
        this.activityContextRef = new WeakReference<>(activityContext);
        this.contextIsActivity = true;
        return this;
    }

    public void toRecipeCollectionsView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.RECIPE_ID, AppContract.Key.NO_ID,
                        obtainFlags(new Intent(context, RecipeCollectionsActivity.class)))
        ); // goto getFragmentCollections();
    }

    public void toRecipeDetailsView(Recipe recipe) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.RECIPE_ID, recipe.getUuid(),
                        obtainFlags(new Intent(context, RecipeDetailsActivity.class)))
        ); // goto getRestriction();
    }

    public void toRecipeListView(Category category) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.CATEGORY_ID, category.getUuid(),
                        obtainFlags(new Intent(context, SingleFragmentActivity.class)))
        ); // goto getFragmentSingle();
    }

    public void toRecipeListView(Label label) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.LABEL_ID, label.getUuid(),
                        obtainFlags(new Intent(context, SingleFragmentActivity.class)))
        ); // goto getFragmentSingle();
    }

    public Fragment[] getFragmentCollections(Intent intent) {
        if (intent.getStringExtra(AppContract.Key.RECIPE_ID) != null) {
            if (intent.getStringExtra(AppContract.Key.RECIPE_ID).equals(AppContract.Key.NO_ID)) {
                return new Fragment[]{
                        RecipeCollectionCategoryFragment.newInstance(),
                        RecipeCollectionGridFragment.newInstance(),
                        RecipeCollectionLabelFragment.newInstance()};
            } else {
                return new Fragment[]{
                        RecipeDetailsDescriptionFragment.newInstance(intent),
                        RecipeDetailsIngredientsFragment.newInstance(intent),
                        RecipeDetailsMethodsFragment.newInstance(intent)
                }; // goto getRestriction();
            }
        }
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
        } else if (intent.getStringExtra(AppContract.Key.RECIPE_ID) != null) {
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.RECIPE_ID),
                    RecipeFull.class
            );
        }
        return null;
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
