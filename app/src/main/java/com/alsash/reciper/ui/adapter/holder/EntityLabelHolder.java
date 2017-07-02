package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Label;

/**
 * A Label item holder
 */
public class EntityLabelHolder extends BaseEntityHolder {

    private final EditText name;
    private final ImageButton valueEdit;

    public EntityLabelHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId);
        name = (EditText) itemView.findViewById(R.id.item_label_name);
        valueEdit = (ImageButton) itemView.findViewById(R.id.item_label_edit);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Label label = (Label) entity;
        name.setText(label.getName());
    }

    @Override
    public String[] getEditable() {
        return new String[]{name.getText().toString()};
    }

    @Override
    public void setEditable(boolean editable) {
        valueEdit.setImageResource(editable ?
                R.drawable.edit_icon_orange :
                R.drawable.edit_icon_gray);
        name.setEnabled(editable);
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. editValuesListener - must implement View.OnClickListener
     */
    @Override
    public void setListeners(Object... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    valueEdit.setOnClickListener((View.OnClickListener) listeners[i]);
                    break;
            }
        }
    }
}
