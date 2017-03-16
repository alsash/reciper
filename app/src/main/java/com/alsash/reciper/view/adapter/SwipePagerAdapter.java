package com.alsash.reciper.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alsash.reciper.R;
import com.alsash.reciper.view.fragment.RecipeListFragment;
import com.alsash.reciper.view.vector.VectorHelper;
import com.alsash.reciper.view.xmlview.SwipeViewPager;

import java.lang.ref.WeakReference;

/**
 * SwipePagerAdapter for use with SwipeViewPager
 */
public class SwipePagerAdapter extends FragmentPagerAdapter
        implements SwipeViewPager.OnPageSelectListener {

    private final WeakReference<Context> contextRef;
    private final VectorHelper vectorHelper;

    public SwipePagerAdapter(Context context, FragmentManager fm) {
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
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public Drawable getPageIcon(int position, @ColorRes int color) {
        Drawable icon;
        switch (position) {
            case 0:
                icon = vectorHelper.create(R.drawable.ic_category);
                break;
            case 1:
                icon = vectorHelper.create(R.drawable.ic_labeled);
                break;
            case 2:
                icon = vectorHelper.create(R.drawable.ic_bookmarked);
                break;
            case 3:
                icon = vectorHelper.create(R.drawable.ic_all);
                break;
            default:
                icon = null;
        }
        return icon;
    }
}
