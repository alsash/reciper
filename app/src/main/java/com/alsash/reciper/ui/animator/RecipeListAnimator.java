package com.alsash.reciper.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.alsash.reciper.ui.adapter.RecipeCardAdapter;
import com.alsash.reciper.ui.adapter.holder.RecipeCardHolder;

import java.util.List;

public class RecipeListAnimator extends DefaultItemAnimator {

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
        // Flip animation. Stage 2 of 4. Record flip direction.
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
        // Flip animation. Stage 4 of 4. Animate flip.
        // NewHolder has the same visibility (front or back) as the previous holder at his position.
        // After animation visibility will be set to the correct value and viewHolder can be reused.
        if (preInfo instanceof FlipInfo) {
            animateFlip(
                    (RecipeCardHolder) newHolder,
                    (FlipInfo) preInfo,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dispatchChangeFinished(newHolder, false);
                            if (newHolder != oldHolder) dispatchChangeFinished(oldHolder, true);
                        }
                    });
            return false;

        }
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    private void animateFlip(@NonNull RecipeCardHolder holder,
                             @NonNull FlipInfo flipInfo,
                             @Nullable AnimatorListenerAdapter listener) {
        holder.getFlipAnimator()
                .setFlipContainer((FrameLayout) holder.itemView)
                .setFrontToBackDirection(flipInfo.isFrontToBack)
                .setListener(listener)
                .flip();
    }

    private class FlipInfo extends ItemHolderInfo {
        public boolean isFrontToBack;

        public FlipInfo setFrontToBack(boolean frontToBack) {
            isFrontToBack = frontToBack;
            return this;
        }
    }

}
