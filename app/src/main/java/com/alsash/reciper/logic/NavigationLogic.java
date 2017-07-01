package com.alsash.reciper.logic;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.ui.activity.FragmentCollectionActivity;
import com.alsash.reciper.ui.activity.FragmentSingleActivity;
import com.alsash.reciper.ui.activity.RecipeDetailsActivity;
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

    public NavigationLogic(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    public NavigationLogic fromActivity(Context activityContext) {
        this.activityContextRef = new WeakReference<>(activityContext);
        return this;
    }

    @IdRes
    @Nullable
    public Integer getNavigationItemId(Intent intent) {

        EntityRestriction restriction = getRestriction(intent);
        if (restriction == null) return null;

        if (restriction.entityUuid.equals(AppContract.Key.NO_ID)) {
            if (restriction.entityClass.equals(Recipe.class))
                return R.id.navigation_app_recipe;

            if (restriction.entityClass.equals(Category.class)
                    || restriction.entityClass.isAssignableFrom(Category.class))
                return R.id.navigation_app_category;

            if (restriction.entityClass.equals(Label.class)
                    || restriction.entityClass.isAssignableFrom(Label.class))
                return R.id.navigation_app_label;

            if (restriction.entityClass.equals(Food.class)
                    || restriction.entityClass.isAssignableFrom(Food.class))
                return R.id.navigation_app_food;

            if (restriction.entityClass.equals(Author.class)
                    || restriction.entityClass.isAssignableFrom(Author.class))
                return R.id.navigation_app_author;
        }

        return null;
    }

    @StringRes
    public Integer getNavigationItemTitleRes(Intent intent) {

        EntityRestriction restriction = getRestriction(intent);
        if (restriction == null) return null;

        if (restriction.entityClass.equals(Recipe.class))
            return R.string.navigation_app_recipe;

        if (restriction.entityClass.equals(Category.class))
            return R.string.navigation_app_category;

        if (restriction.entityClass.equals(Label.class))
            return R.string.navigation_app_label;

        if (restriction.entityClass.equals(Food.class))
            return R.string.navigation_app_food;

        if (restriction.entityClass.equals(Author.class))
            return R.string.navigation_app_author;

        return null;
    }

    public void toNavigationItemId(@IdRes int id) {
        switch (id) {
            case R.id.navigation_app_recipe:
                toRecipesView();
                break;
            case R.id.navigation_app_category:
                toCategoriesView();
                break;
            case R.id.navigation_app_label:
                toLabelsView();
                break;
            case R.id.navigation_app_food:
                toFoodsView();
                break;
            case R.id.navigation_app_author:
                toAuthorsView();
                break;
            default:
                getContext(); // Cleanup activity context
        }
    }

    public void toRecipesView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.RECIPE_ID, AppContract.Key.NO_ID,
                        obtainFlags(context, new Intent(context, FragmentCollectionActivity.class)))
        ); // goto getFragmentCollection();
        // finishIfActivity(context);
    }

    public void toCategoriesView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.CATEGORY_ID, AppContract.Key.NO_ID,
                        obtainFlags(context, new Intent(context, FragmentCollectionActivity.class)))
        ); // goto getFragmentCollection();
        // finishIfActivity(context);
    }

    public void toLabelsView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.LABEL_ID, AppContract.Key.NO_ID,
                        obtainFlags(context, new Intent(context, FragmentCollectionActivity.class)))
        ); // goto getFragmentCollection();
        // finishIfActivity(context);
    }

    public void toFoodsView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.FOOD_ID, AppContract.Key.NO_ID,
                        obtainFlags(context, new Intent(context, FragmentCollectionActivity.class)))
        ); // goto getFragmentCollection();
        // finishIfActivity(context);
    }

    public void toAuthorsView() {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.AUTHOR_ID, AppContract.Key.NO_ID,
                        obtainFlags(context, new Intent(context, FragmentCollectionActivity.class)))
        ); // goto getFragmentCollection();
        //  finishIfActivity(context);
    }

    public void toRecipeDetailsView(Recipe recipe) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.RECIPE_ID, recipe.getUuid(),
                        obtainFlags(context, new Intent(context, RecipeDetailsActivity.class)))
        );
    }

    public void toRecipeListView(Category category) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.CATEGORY_ID, category.getUuid(),
                        obtainFlags(context, new Intent(context, FragmentSingleActivity.class)))
        );
    }

    public void toRecipeListView(Label label) {
        Context context = getContext();
        if (context == null) return;
        context.startActivity(
                obtainRestriction(AppContract.Key.LABEL_ID, label.getUuid(),
                        obtainFlags(context, new Intent(context, FragmentSingleActivity.class)))
        );
    }

    public Fragment[] getFragmentCollection(Intent intent) {

        EntityRestriction restriction = getRestriction(intent);
        if (restriction == null) return null;

        // No ID
        if (restriction.entityUuid.equals(AppContract.Key.NO_ID)) {
            if (restriction.entityClass.equals(Recipe.class))
                return new Fragment[]{
                        RecipeCollectionCategoryFragment.newInstance(),
                        RecipeCollectionGridFragment.newInstance(),
                        RecipeCollectionLabelFragment.newInstance()};

            if (restriction.entityClass.equals(Category.class))
                return new Fragment[]{new Fragment()}; // TODO: Add category list fragment

            if (restriction.entityClass.equals(Label.class))
                return new Fragment[]{new Fragment()}; // TODO: Add label list fragment

            if (restriction.entityClass.equals(Food.class))
                return new Fragment[]{new Fragment()}; // TODO: Add food list fragment

            if (restriction.entityClass.equals(Author.class))
                return new Fragment[]{new Fragment()}; // TODO: Add author list fragment
        }

        // With ID
        if (restriction.entityClass.equals(RecipeFull.class))
            return new Fragment[]{
                    RecipeDetailsDescriptionFragment.newInstance(intent),
                    RecipeDetailsIngredientsFragment.newInstance(intent),
                    RecipeDetailsMethodsFragment.newInstance(intent)};

        return null;
    }

    public Fragment getFragmentSingle(Intent intent) {
        EntityRestriction restriction = getRestriction(intent);
        if (restriction == null) return null;

        if (restriction.entityClass.equals(Category.class)
                || restriction.entityClass.equals(Label.class))
            return RecipeRestrictListFragment.newInstance(intent);

        return null;
    }

    public EntityRestriction getRestriction(Intent intent) {

        if (intent.getStringExtra(AppContract.Key.RECIPE_ID) != null) {
            if (intent.getStringExtra(AppContract.Key.RECIPE_ID).equals(AppContract.Key.NO_ID)) {
                return new EntityRestriction(
                        intent.getStringExtra(AppContract.Key.RECIPE_ID),
                        Recipe.class
                );
            } else {
                return new EntityRestriction(
                        intent.getStringExtra(AppContract.Key.RECIPE_ID),
                        RecipeFull.class
                );
            }
        }

        if (intent.getStringExtra(AppContract.Key.CATEGORY_ID) != null)
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.CATEGORY_ID),
                    Category.class
            );

        if (intent.getStringExtra(AppContract.Key.LABEL_ID) != null)
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.LABEL_ID),
                    Label.class
            );

        if (intent.getStringExtra(AppContract.Key.FOOD_ID) != null)
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.FOOD_ID),
                    Food.class
            );

        if (intent.getStringExtra(AppContract.Key.AUTHOR_ID) != null)
            return new EntityRestriction(
                    intent.getStringExtra(AppContract.Key.AUTHOR_ID),
                    Author.class
            );

        return null;
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

    private Intent obtainRestriction(String restrictionName,
                                     String restrictionValue,
                                     Intent intent) {
        intent.putExtra(restrictionName, restrictionValue);
        return intent;
    }

    private Intent obtainFlags(Context context, Intent intent) {
        if (context instanceof AppCompatActivity) return intent;
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void finishIfActivity(Context context) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.overridePendingTransition(0, 0); // No animation on finishView
            activity.finish();
        }
    }
}
