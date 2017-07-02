package com.alsash.reciper.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageButton;

import com.alsash.reciper.R;

import java.lang.ref.WeakReference;

/**
 * A simple expandable animator helper
 */
public class ExpandAnimatorHelper {

    private boolean expand;
    private int duration;
    private int translation;
    private TimeInterpolator interpolator;
    private WeakReference<ImageButton> buttonRef;
    private WeakReference<ConstraintLayout> layoutRef;

    private ExpandAnimatorHelper() {
    }

    public static ExpandAnimatorHelper get() {
        return new ExpandAnimatorHelper();
    }

    public ExpandAnimatorHelper expand(boolean expand) {
        this.expand = expand;
        return this;
    }

    public ExpandAnimatorHelper durationMillis(int duration) {
        this.duration = duration;
        return this;
    }

    public ExpandAnimatorHelper translationDp(int translation) {
        this.translation = translation;
        return this;
    }

    public ExpandAnimatorHelper interpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public ExpandAnimatorHelper button(ImageButton button) {
        this.buttonRef = new WeakReference<>(button);
        return this;
    }

    public ExpandAnimatorHelper layout(ConstraintLayout layout) {
        this.layoutRef = new WeakReference<>(layout);
        return this;
    }

    public void start() {
        buttonRef.get().clearAnimation();
        layoutRef.get().clearAnimation();
        float density = layoutRef.get().getContext().getResources().getDisplayMetrics().density;
        float[] rotate = new float[]{0f, -180f};
        float[] translateY = new float[]{translation * density * -1, 0f};

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(buttonRef.get(), "rotation",
                rotate[expand ? 0 : 1], rotate[expand ? 1 : 0]);

        ObjectAnimator collapseAnim = ObjectAnimator.ofFloat(layoutRef.get(), "translationY",
                translateY[expand ? 0 : 1], translateY[expand ? 1 : 0]);

        if (interpolator != null) collapseAnim.setInterpolator(interpolator);

        collapseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translation = (float) animation.getAnimatedValue("translationY");
                if (layoutRef.get() == null) return;
                float height = layoutRef.get().getLayoutParams().height;
                layoutRef.get().getLayoutParams().height = Math.round(height + translation);
                layoutRef.get().requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (layoutRef.get() == null) return;
                if (expand) layoutRef.get().setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (layoutRef.get() == null) return;
                if (!expand) layoutRef.get().setVisibility(View.GONE);
                if (buttonRef.get() == null) return;
                buttonRef.get().setImageResource(expand ?
                        R.drawable.expand_icon_orange :
                        R.drawable.expand_icon_gray);
            }
        });
        animatorSet.playTogether(rotationAnim, collapseAnim);
        animatorSet.start();
    }
}
