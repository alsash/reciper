package com.alsash.reciper.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alsash.reciper.R;
import com.alsash.reciper.view.fragment.RecipeCategoryFragment;
import com.alsash.reciper.view.fragment.RecipeListFragment;

/**
 * RecipeCatPagerAdapter that represents two tabs:
 * list of recipe categories and
 * list of single recipes
 */
public class RecipeCatPagerAdapter extends SwipePagerAdapter {

    public RecipeCatPagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public boolean isSwipeEnabled(int position) {
        return position != 0;
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0) ?
                RecipeCategoryFragment.newInstance() :
                RecipeListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources resources = contextRef.get().getResources();
        return (position == 0) ?
                resources.getString(R.string.recipe_group_category) :
                resources.getString(R.string.recipe_group_list);
    }

    @Override
    public Drawable getPageIcon(int position) {
        Drawable icon = (position == 0) ?
                vectorHelper.create(R.drawable.ic_category) :
                vectorHelper.create(R.drawable.ic_all);
        vectorHelper.tintStateList(icon, R.color.cs_tab_icon);
        return icon;
    }
}
