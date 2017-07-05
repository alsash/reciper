package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * Simple category selection holder
 */
public class EntityAuthorSelectionHolder extends BaseEntitySelectionHolder {

    private final ImageView image;
    private final TextView name;

    public EntityAuthorSelectionHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId, R.id.item_author_checkbox);
        image = (ImageView) itemView.findViewById(R.id.item_author_image);
        name = (TextView) itemView.findViewById(R.id.item_author_name);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Author author = (Author) entity;
        ImageLoader.get().source(author).load(image);
        name.setText(author.getName());
    }
}
