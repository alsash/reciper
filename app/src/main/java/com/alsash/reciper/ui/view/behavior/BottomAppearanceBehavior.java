package com.alsash.reciper.ui.view.behavior;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * A CoordinatorLayout.Behavior for appearance and disappearance of the BottomNavigationView
 */
public class BottomAppearanceBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   BottomNavigationView child,
                                   View dependency) {
        return dependency instanceof BottomNavigationView;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       BottomNavigationView child,
                                       View directTargetChild,
                                       View target,
                                       int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  BottomNavigationView child,
                                  View target,
                                  int dx,
                                  int dy,
                                  int[] consumed) {
        if (dy < 0) {
            child.animate().translationY(0); // show BottomNavigationView
        } else if (dy > 0) {
            child.animate().translationY(child.getHeight()); // hide BottomNavigationView
        }
    }
}
