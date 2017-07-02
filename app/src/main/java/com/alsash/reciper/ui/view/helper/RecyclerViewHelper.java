package com.alsash.reciper.ui.view.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.Arrays;

/**
 * Simple helper for RecyclerView calculations
 */
public class RecyclerViewHelper {

    private static final String TAG = "RecyclerViewHelper";

    public static int getLastVisibleItemPosition(RecyclerView.LayoutManager lm) {
        Class<? extends RecyclerView.LayoutManager> lmClass = lm.getClass();
        Class<LinearLayoutManager> linearLmClass = LinearLayoutManager.class;
        Class<StaggeredGridLayoutManager> staggeredLmClass = StaggeredGridLayoutManager.class;

        try {
            if (lmClass == linearLmClass || linearLmClass.isAssignableFrom(lmClass)) {

                return linearLmClass.cast(lm).findLastVisibleItemPosition();

            } else if (lmClass == staggeredLmClass || staggeredLmClass.isAssignableFrom(lmClass)) {

                int[] into = staggeredLmClass.cast(lm).findLastVisibleItemPositions(null);
                if (into.length > 0) {
                    Arrays.sort(into);
                    return into[into.length - 1];
                }
            }
        } catch (ClassCastException e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return -1;
    }
}
