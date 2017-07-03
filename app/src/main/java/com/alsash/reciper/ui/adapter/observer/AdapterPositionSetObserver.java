package com.alsash.reciper.ui.adapter.observer;

import android.support.v7.widget.RecyclerView;

import java.util.Set;

/**
 * Simple adapter data observer that update positions of views on data changes
 */
public class AdapterPositionSetObserver extends RecyclerView.AdapterDataObserver {

    private final Set<Integer>[] positionSets;

    @SafeVarargs
    public AdapterPositionSetObserver(Set<Integer>... positionSets) {
        this.positionSets = positionSets;
    }

    @Override
    public void onChanged() {
        for (Set<Integer> set : positionSets) set.clear();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        if (itemCount <= 0) return;
        for (int i = positionStart; i <= itemCount + positionStart; i++) {
            for (Set<Integer> set : positionSets) set.remove(i);
        }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        if (itemCount <= 0) return;
        for (int i = itemCount + positionStart - 1; i >= positionStart; i--) {
            for (Set<Integer> set : positionSets) {
                if (set.remove(i)) set.add(i + 1);
            }
        }
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        if (itemCount <= 0) return;
        for (int i = positionStart; i <= itemCount + positionStart; i++) {
            for (Set<Integer> set : positionSets) {
                if (set.remove(i + 1)) set.add(i);
            }
        }
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        if (fromPosition == toPosition) return;
        if (itemCount != 1) return; // Google says: Moving more than 1 item is not supported yet :)
        for (Set<Integer> set : positionSets) {
            boolean fromExist = set.contains(fromPosition);
            boolean toExist = set.contains(toPosition);
            if (fromExist != toExist) {
                if (fromExist) {
                    set.remove(fromPosition);
                    set.add(toPosition);
                }
                if (toExist) {
                    set.remove(toPosition);
                    set.add(fromPosition);
                }
            }
        }
    }
}
