package com.alsash.reciper.ui.adapter.holder;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.v4.content.res.ResourcesCompat;
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
    private final int checkedColor;
    private final int uncheckedColor;

    public EntityLabelSelectionHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId, null);
        name = (TextView) itemView.findViewById(R.id.item_label_name);
        Resources resources = name.getResources();
        Resources.Theme theme = name.getContext().getTheme();
        checkedColor = ResourcesCompat.getColor(resources, R.color.orange_500, theme);
        uncheckedColor = ResourcesCompat.getColor(resources, R.color.gray_900, theme);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Label label = (Label) entity;
        name.setText(label.getName());
    }

    @Override
    public void setChecked(boolean checked) {
        name.setBackgroundResource(checked ?
                R.drawable.label_recipe_shape_orange :
                R.drawable.label_recipe_shape_gray);
        name.setTextColor(checked ? checkedColor : uncheckedColor);
    }
}
