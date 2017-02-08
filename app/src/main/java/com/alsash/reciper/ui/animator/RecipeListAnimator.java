package com.alsash.reciper.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.alsash.reciper.ui.adapter.RecipeCardAdapter;
import com.alsash.reciper.ui.adapter.holder.RecipeCardHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeListAnimator extends DefaultItemAnimator {

    private Map<RecyclerView.ViewHolder, FlipAnimatorHelper> flipAnimationsMap = new HashMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof RecipeCardHolder) {
            return true;
        } else {
            return super.canReuseUpdatedViewHolder(viewHolder);
        }
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags,
                                                     @NonNull List<Object> payloads) {
        // Flip animation. Stage 2 of 3. Record flip direction.
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (RecipeCardAdapter.PAYLOAD_FLIP_FRONT_TO_BACK.equals(payload)) {
                    return new FlipInfo().setFrontToBack(true).setFrom(viewHolder);
                } else if (RecipeCardAdapter.PAYLOAD_FLIP_BACK_TO_FRONT.equals(payload)) {
                    return new FlipInfo().setFrontToBack(false).setFrom(viewHolder);
                }
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateChange(@NonNull final RecyclerView.ViewHolder oldHolder,
                                 @NonNull final RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {

        // Flip animation. Stage 3 of 3. Animate flip.
        if (preInfo instanceof FlipInfo) {
            FlipAnimatorHelper flipHelper = flipAnimationsMap.get(newHolder);
            if (flipHelper == null) {
                flipHelper = new FlipAnimatorHelper(newHolder.itemView.getContext());
                flipAnimationsMap.put(newHolder, flipHelper);
            } else {
                flipHelper.endIfStarted();
            }
            flipHelper
                    .setFlipContainer((FrameLayout) newHolder.itemView)
                    .setFrontToBackDirection(((FlipInfo) preInfo).isFrontToBack)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dispatchChangeFinished(newHolder, false);
                            if (newHolder != oldHolder) dispatchChangeFinished(oldHolder, true);
                            flipAnimationsMap.remove(newHolder);
                        }
                    })
                    .flip();
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

    private class FlipInfo extends ItemHolderInfo {
        public boolean isFrontToBack;

        public FlipInfo setFrontToBack(boolean frontToBack) {
            isFrontToBack = frontToBack;
            return this;
        }
    }

}
