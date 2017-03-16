package com.alsash.reciper.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alsash.reciper.view.vector.VectorHelper;
import com.alsash.reciper.view.xmlview.SwipeViewPager;

import java.lang.ref.WeakReference;

public abstract class SwipePagerAdapter extends FragmentPagerAdapter
        implements SwipeViewPager.OnPageSelectListener {

    protected final WeakReference<Context> contextRef;
    protected final VectorHelper vectorHelper;

    public SwipePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.contextRef = new WeakReference<>(context);
        this.vectorHelper = new VectorHelper(context);
    }

    public abstract boolean isSwipeEnabled(int position);

    public abstract Drawable getPageIcon(int position);
}
