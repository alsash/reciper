package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.ui.adapter.holder.RecipeLabelHolder;
import com.alsash.reciper.ui.adapter.interaction.LabelInteraction;

import java.util.List;

/**
 * Simple list adapter
 */
public class RecipeLabelListAdapter extends RecyclerView.Adapter<RecipeLabelHolder> {

    private final List<Label> labels;
    private final LabelInteraction interaction;

    public RecipeLabelListAdapter(List<Label> labels, LabelInteraction interaction) {
        this.labels = labels;
        this.interaction = interaction;
    }

    @Override
    public RecipeLabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeLabelHolder(parent, R.layout.item_label_recipe);
    }

    @Override
    public void onBindViewHolder(final RecipeLabelHolder holder, int position) {
        holder.labelName.setText(labels.get(position).getName());
        holder.labelName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interaction.onPress(labels.get(holder.getAdapterPosition()));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }
}
