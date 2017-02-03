package com.alsash.reciper.ui.animator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.ui.adapter.RecipeListAdapter;

import java.util.List;

public class RecipeListAnimator extends DefaultItemAnimator {

    private long recipe;

    private RecyclerView.ViewHolder appearanceHolder;
    private RecyclerView.ViewHolder disappearanceHolder;


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
        for (Object payload : payloads) {
            if (RecipeListAdapter.PAYLOAD_FLIP.equals(payload)) {
                return new FlipInfo().setFrom(viewHolder);
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                        @NonNull ItemHolderInfo preLayoutInfo,
                                        @Nullable ItemHolderInfo postLayoutInfo) {
        if (preLayoutInfo instanceof FlipInfo) {
            disappearanceHolder = viewHolder;
            return true;
        }
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                     @Nullable ItemHolderInfo preLayoutInfo,
                                     @NonNull ItemHolderInfo postLayoutInfo) {
        if (postLayoutInfo instanceof FlipInfo) {
            appearanceHolder = viewHolder;
            return true;
        }
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
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    @Override
    public void runPendingAnimations() {
        super.runPendingAnimations();
    }

    private class FlipInfo extends ItemHolderInfo {
    }

}
