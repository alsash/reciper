package com.alsash.reciper.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.alsash.reciper.ui.adapter.RecipeCardAdapter;
import com.alsash.reciper.ui.adapter.holder.RecipeCardHolder;

import java.util.List;

public class RecipeListAnimator extends DefaultItemAnimator {

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags,
                                                     @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (RecipeCardAdapter.PAYLOAD_FLIP.equals(payload)) {
                    RecipeCardHolder oldCardHolder = (RecipeCardHolder) viewHolder;
                    return new FlipInfo(oldCardHolder.isBackVisible()).setFrom(viewHolder);
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
        if (preInfo instanceof FlipInfo) {
            FlipInfo flipInfo = (FlipInfo) preInfo;
            final boolean isOldEqual = (oldHolder == newHolder);
            new FlipAnimatorHelper(newHolder.itemView.getContext())
                    .setFlipContainer((FrameLayout) newHolder.itemView)
                    .setToFrontDirection(flipInfo.prevBackVisible)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dispatchChangeFinished(newHolder, false);
                            if (!isOldEqual) {
                                dispatchChangeFinished(oldHolder, true);
                            }
                        }
                    }).flip();
            return false;
        }
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    private class FlipInfo extends ItemHolderInfo {
        public final boolean prevBackVisible;

        public FlipInfo(boolean prevBackVisible) {
            this.prevBackVisible = prevBackVisible;
        }
    }

}
