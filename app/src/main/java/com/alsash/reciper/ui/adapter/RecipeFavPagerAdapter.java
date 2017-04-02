package com.alsash.reciper.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.fragment.RecipeSingleListFragment;

/**
 * RecipeFavPagerAdapter that represents tabs with user's favorite recipes:
 * list of labeled recipes and
 * list of bookmarked recipes
 */
public class RecipeFavPagerAdapter extends SwipePagerAdapter {

    public RecipeFavPagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public boolean isSwipeEnabled(int position) {
        return position != 0;
    }

    @Override
    public Fragment getItem(int position) {
        return RecipeSingleListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources resources = contextRef.get().getResources();
        return (position == 0) ?
                resources.getString(R.string.recipe_group_label) :
                resources.getString(R.string.recipe_group_bookmark);
    }

    @Override
    public Drawable getPageIcon(int position) {
        Drawable icon = (position == 0) ?
                vectorHelper.create(R.drawable.ic_labeled) :
                vectorHelper.create(R.drawable.ic_bookmarked);
        vectorHelper.tintStateList(icon, R.color.cs_tab_icon);
        return icon;
    }
}
