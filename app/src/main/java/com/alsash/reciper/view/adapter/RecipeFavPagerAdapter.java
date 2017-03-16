package com.alsash.reciper.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alsash.reciper.R;
import com.alsash.reciper.view.fragment.RecipeListFragment;
import com.alsash.reciper.view.vector.VectorHelper;
import com.alsash.reciper.view.xmlview.SwipeViewPager;

import java.lang.ref.WeakReference;

/**
 * RecipeFavPagerAdapter that represents tabs with user's favorite recipes:
 * list of labeled recipes and
 * list of bookmarked recipes
 */
public class RecipeFavPagerAdapter extends FragmentPagerAdapter
        implements SwipeViewPager.OnPageSelectListener {

    private final WeakReference<Context> contextRef;
    private final VectorHelper vectorHelper;

    public RecipeFavPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.contextRef = new WeakReference<>(context);
        this.vectorHelper = new VectorHelper(context);
    }

    @Override
    public boolean isSwipeEnabled(int position) {
        switch (position) {
            case 0:
                return false;
            default:
                return true;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return RecipeListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources resources = contextRef.get().getResources();
        switch (position) {
            case 0:
                return resources.getString(R.string.recipe_group_label);
            case 1:
                return resources.getString(R.string.recipe_group_bookmark);
        }
        return null;
    }

    public Drawable getPageIcon(int position) {
        Drawable icon;
        switch (position) {
            case 0:
                icon = vectorHelper.create(R.drawable.ic_labeled);
                break;
            case 1:
                icon = vectorHelper.create(R.drawable.ic_bookmarked);
                break;
            case 3:
                icon = vectorHelper.create(R.drawable.ic_all);
                break;
            default:
                icon = null;
        }
        vectorHelper.tintStateList(icon, R.color.cs_tab_icon);
        return icon;
    }
}
