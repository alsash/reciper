package com.alsash.reciper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;

/**
 * Custom ViewPager that toggle handling motion events
 * Used to disable swipes on viewPager
 */
public class SwipeViewPager extends ViewPager {

    private boolean isSwipeEnabled;

    public SwipeViewPager(Context context) {
        super(context);
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Retrieve attributes from xml
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SwipeViewPager);
        try {
            setSwipeEnabled(typedArray.getBoolean(R.styleable.SwipeViewPager_svp_is_swipe_enabled,
                    false));
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isSwipeEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isSwipeEnabled && super.onInterceptTouchEvent(event);
    }

    public void setAdapter(SwipePagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter.hasSwipeBehaviour()) {
            addOnPageChangeListener(new SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    OnPageSelectListener listener = (OnPageSelectListener) getAdapter();
                    setSwipeEnabled(listener.isSwipeEnabled(position));
                }
            });
        }
    }

    public boolean isSwipeEnabled() {
        return isSwipeEnabled;
    }

    public void setSwipeEnabled(boolean isSwipeEnabled) {
        this.isSwipeEnabled = isSwipeEnabled;
    }

    public interface OnPageSelectListener {
        boolean isSwipeEnabled(int position);
    }
}
