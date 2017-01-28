package com.alsash.reciper.ui.animator;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.alsash.reciper.R;

/**
 * Helper class for flipping two children of a {@link FrameLayout}
 * with animation like a poker card flip
 * Uses modern {@link AnimatorSet} class instead of an old {@link android.view.animation.Animation}
 */
public class FlipAnimatorHelper {

    private FrameLayout flipContainer;

    private boolean isBackVisible;
    private AnimatorSet flipLeftIn;
    private AnimatorSet flipLeftOut;
    private AnimatorSet flipRightIn;
    private AnimatorSet flipRightOut;

    public FlipAnimatorHelper(FrameLayout frameLayout) {
        flipContainer = frameLayout;
        if (flipContainer.getChildCount() > 0) flipContainer.getChildAt(0).setVisibility(View.GONE);
        inflateAnimators(frameLayout.getContext());
    }

    /**
     * Flip two first children of a flipContainer
     * added into {@link #FlipAnimatorHelper(FrameLayout)}
     *
     * @return visibility of a first child
     */
    public boolean flip() {
        if (flipContainer.getChildCount() < 2) return isBackVisible;
        View frontView = flipContainer.getChildAt(1);
        View backView = flipContainer.getChildAt(0);
        endAll();
        if (isBackVisible) {
            return flip(backView, frontView, flipRightIn, flipRightOut);
        } else {
            return flip(frontView, backView, flipLeftIn, flipLeftOut);
        }
    }

    public boolean isBackVisible() {
        return isBackVisible;
    }

    private boolean flip(final View front, final View back, AnimatorSet in, AnimatorSet out) {
        in.setTarget(back);
        out.setTarget(front);
        in.start();
        out.start();
        isBackVisible = !isBackVisible;
        return isBackVisible;
    }

    private void endAll() {
        if (flipLeftIn.isStarted()) flipLeftIn.end();
        if (flipLeftOut.isStarted()) flipLeftOut.end();
        if (flipRightIn.isStarted()) flipRightIn.end();
        if (flipRightOut.isStarted()) flipRightOut.end();
    }

    private void inflateAnimators(Context context) {
        flipLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        flipLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        flipRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);
        flipRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
    }
}
