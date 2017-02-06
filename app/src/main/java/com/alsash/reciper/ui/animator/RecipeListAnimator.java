package com.alsash.reciper.ui.animator;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alsash.reciper.ui.adapter.holder.RecipeListCardHolder;

public class RecipeListAnimator extends DefaultItemAnimator {

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override
    public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull ItemHolderInfo preInfo,
                                      @NonNull ItemHolderInfo postInfo) {
        if (viewHolder instanceof RecipeListCardHolder) {
            return animateFlip((RecipeListCardHolder) viewHolder);
        }
        return super.animatePersistence(viewHolder, preInfo, postInfo);
    }

    private boolean animateFlip(RecipeListCardHolder cardHolder) {
        if (cardHolder.isFlipNeed) {
            Toast.makeText(cardHolder.itemView.getContext(),
                    "Flip at pos: " + cardHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            cardHolder.isFlipped = true;
        }
        dispatchMoveFinished(cardHolder);
        return false;
    }

    private class FlipInfo extends ItemHolderInfo {
    }

}
