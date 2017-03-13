package com.alsash.reciper.view.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Custom ViewPager that toggle handling motion events
 * Used to disable swipes on viewPager
 */
public class MotionEventViewPager extends ViewPager {

    private boolean isMotionEventEnabled;

    public MotionEventViewPager(Context context) {
        super(context);
    }

    public MotionEventViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isMotionEventEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isMotionEventEnabled && super.onInterceptTouchEvent(event);
    }

    public boolean isMotionEventEnabled() {
        return isMotionEventEnabled;
    }

    public void setMotionEventEnabled(boolean isSwipeEnabled) {
        this.isMotionEventEnabled = isSwipeEnabled;
    }
}
