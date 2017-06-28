package com.alsash.reciper.ui.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Simple expandable helper
 */
public class ExpandableViewHelper {

    public static void expand(View view) {
        // view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        // view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);
        view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.requestLayout();

        Animation animation = new ExpandAnimation(view);
        animation.setDuration(getDuration(view, 4));
        view.startAnimation(animation);
    }

    public static void collapse(final View view) {
        Animation animation = new CollapseAnimation(view);
        animation.setDuration(getDuration(view, 4));
        view.startAnimation(animation);
    }

    /**
     * Calculate duration = factor * dp/ms
     *
     * @param view target view
     * @return duration in milliseconds
     */
    private static int getDuration(View view, int factor) {
        float density = view.getContext().getResources().getDisplayMetrics().density;
        int viewHeight = view.getMeasuredHeight();
        return factor * Math.round(viewHeight / density);
    }

    private static class ExpandAnimation extends Animation {
        protected final View view;
        protected final int targetHeight;

        public ExpandAnimation(View view) {
            this.view = view;
            this.targetHeight = view.getMeasuredHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = interpolatedTime == 1
                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                    : (int) (targetHeight * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private static class CollapseAnimation extends Animation {

        protected final View view;
        protected final int initialHeight;

        public CollapseAnimation(View view) {
            this.view = view;
            this.initialHeight = view.getMeasuredHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (interpolatedTime == 1) {
                view.setVisibility(View.GONE);
            } else {
                view.getLayoutParams().height = initialHeight
                        - (int) (initialHeight * interpolatedTime);
                view.requestLayout();
            }
        }
    }
}
