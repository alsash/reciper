package com.alsash.reciper.ui.animator;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.alsash.reciper.R;

public class FlipAnimatorHelper {

    private FrameLayout flipContainer;

    private boolean isBackVisible;
    private AnimatorSet flipLeftIn;
    private AnimatorSet flipLeftOut;
    private AnimatorSet flipRightIn;
    private AnimatorSet flipRightOut;

    public FlipAnimatorHelper(FrameLayout frameLayout) {
        flipContainer = frameLayout;
        inflateAnimators(frameLayout.getContext());
    }

    public void flip() {
        flip(false);
    }

    public void reset() {
        if (isBackVisible) flip(true);
    }

    private void inflateAnimators(Context context) {
        flipLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        flipLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
        flipRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        flipRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out);
    }

    private void flip(boolean reset) {
        if (flipContainer.getChildCount() < 2) return;
        View frontView = flipContainer.getChildAt(1);
        View backView = flipContainer.getChildAt(0);
        if (isBackVisible) {
            flip(backView, frontView, flipRightIn, flipLeftOut, reset);
        } else {
            flip(frontView, backView, flipRightIn, flipLeftOut, reset);
        }
    }

    private void flip(View front, View back, AnimatorSet in, AnimatorSet out, boolean reset) {
        in.setTarget(back);
        out.setTarget(front);
        if (reset) {
            in.setDuration(0);
            out.setDuration(0);
        }
        in.start();
        out.start();
        if (reset) {
            inflateAnimators(front.getContext());
        }
        isBackVisible = !isBackVisible;
    }
}
