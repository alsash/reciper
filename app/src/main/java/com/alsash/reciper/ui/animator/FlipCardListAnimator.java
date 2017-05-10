package com.alsash.reciper.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.alsash.reciper.ui.adapter.holder.RecipeSingleCardHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alsash.reciper.app.AppContract.PAYLOAD_FLIP_BACK_TO_FRONT;
import static com.alsash.reciper.app.AppContract.PAYLOAD_FLIP_FRONT_TO_BACK;

public class FlipCardListAnimator extends DefaultItemAnimator {

    private Map<RecyclerView.ViewHolder, FlipAnimatorHelper> flipAnimationsMap = new HashMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder holder) {
        return (holder instanceof RecipeSingleCardHolder) || super.canReuseUpdatedViewHolder(holder);
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder holder,
                                                     int changeFlags,
                                                     @NonNull List<Object> payloads) {
        // Flip animation. Stage 2 of 3. Record flip direction.
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (PAYLOAD_FLIP_FRONT_TO_BACK.equals(payload)) {
                    return new FlipInfo().setFrontToBack(true).setFrom(holder);
                } else if (PAYLOAD_FLIP_BACK_TO_FRONT.equals(payload)) {
                    return new FlipInfo().setFrontToBack(false).setFrom(holder);
                }
            }
        }
        return super.recordPreLayoutInformation(state, holder, changeFlags, payloads);
    }

    @Override
    public boolean animateChange(@NonNull final RecyclerView.ViewHolder oldHolder,
                                 @NonNull final RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {

        // Flip animation. Stage 3 of 3. Animate flip.
        if (preInfo instanceof FlipInfo) {
            animateFlip(oldHolder, newHolder, (FlipInfo) preInfo);
            return false;
        }
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        if (flipAnimationsMap.containsKey(item)) {
            flipAnimationsMap.get(item).endIfStarted();
            flipAnimationsMap.remove(item);
        }
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for (FlipAnimatorHelper flipHelper : flipAnimationsMap.values()) {
            flipHelper.endIfStarted();
        }
        flipAnimationsMap.clear();
    }

    private void animateFlip(@NonNull final RecyclerView.ViewHolder oldHolder,
                             @NonNull final RecyclerView.ViewHolder newHolder,
                             @NonNull FlipInfo flipInfo) {

        AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchChangeFinished(newHolder, false);
                if (newHolder != oldHolder) dispatchChangeFinished(oldHolder, true);
                if (animation != null) flipAnimationsMap.remove(newHolder);
            }
        };

        FrameLayout flipContainer;
        if (newHolder.itemView instanceof FrameLayout) {
            flipContainer = (FrameLayout) newHolder.itemView;
        } else {
            animatorListener.onAnimationEnd(null);
            return;
        }

        FlipAnimatorHelper flipHelper = flipAnimationsMap.get(newHolder);
        if (flipHelper == null) {
            flipHelper = new FlipAnimatorHelper(newHolder.itemView.getContext());
            flipAnimationsMap.put(newHolder, flipHelper);
        }

        flipHelper.setFlipContainer(flipContainer)
                .setFrontToBackDirection(flipInfo.isFrontToBack)
                .setListener(animatorListener)
                .flip();
    }

    private static class FlipInfo extends ItemHolderInfo {
        boolean isFrontToBack;

        FlipInfo setFrontToBack(boolean frontToBack) {
            isFrontToBack = frontToBack;
            return this;
        }
    }
}
