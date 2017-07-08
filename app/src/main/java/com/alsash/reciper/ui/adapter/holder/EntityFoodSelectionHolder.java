package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Food;

/**
 * Simple category selection holder
 */
public class EntityFoodSelectionHolder extends BaseEntitySelectionHolder {

    private final ImageView image;
    private final TextView name;

    public EntityFoodSelectionHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId, R.id.item_food_checkbox);
        image = (ImageView) itemView.findViewById(R.id.item_food_image);
        name = (TextView) itemView.findViewById(R.id.item_food_name);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Food food = (Food) entity;
        name.setText(food.getName());
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        image.setImageResource(checked ?
                R.drawable.food_icon_orange :
                R.drawable.food_icon_gray);
    }
}
