package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.reciper.R;

/**
 * Simple label holder
 */
public class RecipeLabelHolder extends RecyclerView.ViewHolder {

    public final TextView labelName;

    public RecipeLabelHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        labelName = (TextView) itemView.findViewById(R.id.item_label_recipe_name);
    }
}
