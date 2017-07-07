package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Label;

/**
 * Simple category selection holder
 */
public class EntityLabelSelectionHolder extends BaseEntitySelectionHolder {

    private final TextView name;

    public EntityLabelSelectionHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId, R.id.item_label_checkbox);
        name = (TextView) itemView.findViewById(R.id.item_label_name);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Label label = (Label) entity;
        name.setText(label.getName());
    }
}
