package com.alsash.reciper.ui.adapter.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Food;

/**
 * A Food item holder
 */
public class EntityFoodHolder extends BaseEntityHolder {

    private final EditText name;
    private final EditText ndbno;
    private final TextView protein;
    private final TextView fat;
    private final TextView carbs;
    private final TextView energy;
    private final ImageButton valueEdit;

    public EntityFoodHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId);
        name = (EditText) itemView.findViewById(R.id.item_food_name);
        ndbno = (EditText) itemView.findViewById(R.id.item_food_ndbno);
        protein = (TextView) itemView.findViewById(R.id.item_food_protein);
        fat = (TextView) itemView.findViewById(R.id.item_food_fat);
        carbs = (TextView) itemView.findViewById(R.id.item_food_carbs);
        energy = (TextView) itemView.findViewById(R.id.item_food_energy);
        valueEdit = (ImageButton) itemView.findViewById(R.id.item_food_edit);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Food food = (Food) entity;
        Context context = name.getContext();
        name.setText(food.getName());
        ndbno.setText(food.getNdbNo());
        protein.setText(context.getString(R.string.quantity_food_protein_gram,
                (float) food.getProtein() * 100));
        fat.setText(context.getString(R.string.quantity_food_fat_gram,
                (float) food.getFat() * 100));
        carbs.setText(context.getString(R.string.quantity_food_carbs_gram,
                (float) food.getCarbs() * 100));
        energy.setText(context.getString(R.string.quantity_food_energy_calorie,
                (int) Math.round(food.getEnergy() * 100)));
    }

    @Override
    public String[] getEditable() {
        return new String[]{
                name.getText().toString(),
                ndbno.getText().toString()
        };
    }

    @Override
    public void setEditable(boolean editable) {
        valueEdit.setImageResource(editable ?
                R.drawable.edit_icon_orange :
                R.drawable.edit_icon_gray);
        name.setEnabled(editable);
        ndbno.setEnabled(editable);
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
