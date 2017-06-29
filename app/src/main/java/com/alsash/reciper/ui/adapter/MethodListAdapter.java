package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.ui.adapter.holder.MethodHolder;
import com.alsash.reciper.ui.adapter.interaction.MethodInteraction;

import java.util.List;

/**
 * A recipe method list adapter
 */
public class MethodListAdapter extends RecyclerView.Adapter<MethodHolder> {

    private final MethodInteraction interaction;
    private final List<Method> methods;

    public MethodListAdapter(MethodInteraction interaction, List<Method> methods) {
        this.interaction = interaction;
        this.methods = methods;
    }

    @Override
    public MethodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MethodHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
