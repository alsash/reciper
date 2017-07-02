package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * A Category item holder
 */
public class EntityCategoryHolder extends BaseEntityHolder {

    private final ImageView image;
    private final EditText name;
    private final ProgressBar imageBar;
    private final ImageButton imageEdit;
    private final ImageButton valueEdit;

    public EntityCategoryHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId);
        image = (ImageView) itemView.findViewById(R.id.item_category_image);
        name = (EditText) itemView.findViewById(R.id.item_category_name);
        imageBar = (ProgressBar) itemView.findViewById(R.id.item_category_bar);
        imageEdit = (ImageButton) itemView.findViewById(R.id.item_category_image_edit);
        valueEdit = (ImageButton) itemView.findViewById(R.id.item_category_edit);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Category category = (Category) entity;
        ImageLoader.get().source(category.getPhoto()).bar(imageBar).load(image);
        name.setText(category.getName());
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
        imageEdit.setVisibility(editable ? View.VISIBLE : View.GONE);
        if (editable) name.requestFocus();
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. editValuesListener - must implement View.OnClickListener
     *                  1. editPhotoListener - must implement View.OnClickListener
     */
    @Override
    public void setListeners(Object... listeners) {
        for (int i = 0; i < listeners.length; i++) {
            switch (i) {
                case 0:
                    valueEdit.setOnClickListener((View.OnClickListener) listeners[i]);
                    break;
                case 1:
                    imageEdit.setOnClickListener((View.OnClickListener) listeners[i]);
                    break;
            }
        }
    }
}
