package com.alsash.reciper.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.alsash.reciper.R;

/**
 * Helper class for flipping two children of a {@link FrameLayout}
 * with animation like a poker card flip
 * Uses the {@link AnimatorSet} class instead of the {@link android.view.animation.Animation}
 */
public class FlipAnimatorHelper {

    private static final int FRONT_INDEX = 1;
    private static final int BACK_INDEX = 0;

    private final AnimatorSet flipLeftIn;
    private final AnimatorSet flipLeftOut;
    private final AnimatorSet flipRightIn;
    private final AnimatorSet flipRightOut;

    private FrameLayout flipContainer;
    private boolean isFrontToBackDirection;

    private Animator.AnimatorListener listener;

    public FlipAnimatorHelper(Context context) {
        flipLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        flipLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        flipRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);
        flipRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
    }

    public FlipAnimatorHelper setFlipContainer(FrameLayout flipContainer) {
        this.flipContainer = flipContainer;
        return this;
    }

    public FlipAnimatorHelper setFrontToBackDirection(boolean frontToBackDirection) {
        isFrontToBackDirection = frontToBackDirection;
        return this;
    }

    public FlipAnimatorHelper setListener(Animator.AnimatorListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Flip two first children of a flipContainer
     * added into {@link #setFlipContainer(FrameLayout)}
     */
    public void flip() {
        if (flipContainer.getChildCount() < 2) return;

        View frontView = flipContainer.getChildAt(FRONT_INDEX);
        View backView = flipContainer.getChildAt(BACK_INDEX);

        if (isFrontToBackDirection) {
            // Front to back
            flip(frontView, backView, flipLeftIn, flipLeftOut);
        } else {
            // Back to front
            flip(backView, frontView, flipRightIn, flipRightOut);
        }
    }

    public void endIfStarted() {
        if (flipLeftIn.isStarted()) flipLeftIn.end();
        if (flipLeftOut.isStarted()) flipLeftOut.end();
        if (flipRightIn.isStarted()) flipRightIn.end();
        if (flipRightOut.isStarted()) flipRightOut.end();
    }

    private void flip(View outView, View inView, AnimatorSet in, AnimatorSet out) {
        endIfStarted();
        out.removeAllListeners();
        if (listener != null) {
            out.addListener(listener);
        }
        in.setTarget(inView);
        out.setTarget(outView);
        in.start();
        out.start();
    }
}
