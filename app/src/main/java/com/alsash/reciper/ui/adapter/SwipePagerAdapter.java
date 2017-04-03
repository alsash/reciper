package com.alsash.reciper.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alsash.reciper.ui.vector.VectorHelper;
import com.alsash.reciper.ui.view.SwipeViewPager;

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

    @Nullable
    public abstract Drawable getPageIcon(int position);
}
