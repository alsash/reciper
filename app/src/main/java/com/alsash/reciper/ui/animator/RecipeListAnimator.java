package com.alsash.reciper.ui.animator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class RecipeListAnimator extends DefaultItemAnimator {

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags,
                                                     @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    return obtainHolderInfo((String) payload, viewHolder);
                }
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                        @NonNull ItemHolderInfo preLayoutInfo,
                                        @Nullable ItemHolderInfo postLayoutInfo) {
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                     @Nullable ItemHolderInfo preLayoutInfo,
                                     @NonNull ItemHolderInfo postLayoutInfo) {
        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull ItemHolderInfo preInfo,
                                      @NonNull ItemHolderInfo postInfo) {
        return super.animatePersistence(viewHolder, preInfo, postInfo);
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        if (oldHolder == newHolder) {
            //  Toast.makeText(newHolder.itemView.getContext(), "equal!", Toast.LENGTH_SHORT).show();
        }
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    private ItemHolderInfo obtainHolderInfo(String payload, RecyclerView.ViewHolder holder) {
        return new CardHolderInfo(payload).setFrom(holder);
    }

    private class CardHolderInfo extends ItemHolderInfo {
        public final String payload;

        public CardHolderInfo(String payload) {
            this.payload = payload;
        }
    }

}
