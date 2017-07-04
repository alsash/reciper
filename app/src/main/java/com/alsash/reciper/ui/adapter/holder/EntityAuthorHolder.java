package com.alsash.reciper.ui.adapter.holder;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * An Author item holder
 */
public class EntityAuthorHolder extends BaseEntityHolder {

    private final ImageView image;
    private final EditText name;
    private final Drawable nameBackground;
    private final ImageButton imageEdit;
    private final ImageButton valueEdit;

    public EntityAuthorHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId);
        image = (ImageView) itemView.findViewById(R.id.item_author_image);
        name = (EditText) itemView.findViewById(R.id.item_author_name);
        nameBackground = name.getBackground();
        imageEdit = (ImageButton) itemView.findViewById(R.id.item_author_image_edit);
        valueEdit = (ImageButton) itemView.findViewById(R.id.item_author_edit);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Author author = (Author) entity;
        ImageLoader.get().source(author).load(image);
        name.setText(author.getName());
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
        name.setFocusable(editable);
        name.setFocusableInTouchMode(editable);
        name.setClickable(editable);
        name.setLongClickable(editable);
        imageEdit.setVisibility(editable ? View.VISIBLE : View.GONE);
        if (editable) {
            name.setBackground(nameBackground);
            name.requestFocus();
        } else {
            name.setBackground(null);
        }
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
