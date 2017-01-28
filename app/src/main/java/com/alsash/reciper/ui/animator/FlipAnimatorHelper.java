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

    private final int frontIndex = 1;
    private final int backIndex = 0;

    private final FrameLayout flipContainer;

    private boolean isBackVisible;
    private AnimatorSet flipLeftIn;
    private AnimatorSet flipLeftOut;
    private AnimatorSet flipRightIn;
    private AnimatorSet flipRightOut;

    public FlipAnimatorHelper(final FrameLayout frameLayout, boolean isBackVisible) {
        this.flipContainer = frameLayout;
        this.isBackVisible = isBackVisible;
        inflateAnimators(frameLayout.getContext());

        // Set start invisibility to front or back
        if (flipContainer.getChildCount() > 1) {
            int index = isBackVisible ? frontIndex : backIndex;
            flipContainer.getChildAt(index).setVisibility(View.GONE);
        }
    }

    /**
     * Flip two first children of a flipContainer
     * added into {@link #FlipAnimatorHelper(FrameLayout, boolean)}
     *
     * @param immediately end animation immediately
     * @return visibility of a first child
     */
    public boolean flip(boolean immediately) {
        if (flipContainer.getChildCount() < 2) return isBackVisible;
        View frontView = flipContainer.getChildAt(frontIndex);
        View backView = flipContainer.getChildAt(backIndex);
        if (isBackVisible) {
            return flip(backView, frontView, flipRightIn, flipRightOut, immediately);
        } else {
            return flip(frontView, backView, flipLeftIn, flipLeftOut, immediately);
        }
    }

    /**
     * See {@link #flip(boolean)}
     */
    public boolean flip() {
        return flip(false);
    }

    /**
     * Getter for visibility
     *
     * @return visibility of a first child
     */
    public boolean isBackVisible() {
        return isBackVisible;
    }

    private boolean flip(final View front, final View back, AnimatorSet in, AnimatorSet out, boolean end) {
        if (in.isStarted()) in.end();
        if (out.isStarted()) out.end();
        in.setTarget(back);
        out.setTarget(front);
        in.start();
        out.start();
        if (end) {
            in.end();
            out.end();
        }
        isBackVisible = !isBackVisible;
        return isBackVisible;
    }

    private void inflateAnimators(Context context) {
        flipLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        flipLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        flipRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);
        flipRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out);
    }
}
