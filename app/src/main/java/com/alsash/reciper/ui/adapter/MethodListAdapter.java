package com.alsash.reciper.ui.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.ui.adapter.holder.MethodHolder;
import com.alsash.reciper.ui.adapter.interaction.MethodInteraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A recipe method list adapter
 */
public class MethodListAdapter extends RecyclerView.Adapter<MethodHolder> {

    private final MethodInteraction interaction;
    private final List<Method> methods;
    private final Set<Integer> editPositions;

    public MethodListAdapter(MethodInteraction interaction, List<Method> methods) {
        this.interaction = interaction;
        this.methods = methods;
        this.editPositions = new HashSet<>();
    }

    @Override
    public MethodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MethodHolder(parent, R.layout.item_method_card);
    }

    @Override
    public void onBindViewHolder(final MethodHolder holder, int position) {
        holder.setEditable(editPositions.contains(position));
        holder.bindMethod(methods.get(position));
        holder.setListeners(
                // Edit listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        boolean editable = !editPositions.remove(position)
                                && editPositions.add(position);
                        holder.setEditable(editable);
                        if (!editable)
                            interaction.onMethodEdit(methods.get(position), holder.getBody());
                    }
                },
                // Drag listener
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            interaction.onMethodDrag(holder);
                        }
                        return false;
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return methods.size();
    }
}
