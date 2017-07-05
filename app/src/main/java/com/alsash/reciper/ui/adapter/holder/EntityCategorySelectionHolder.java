package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * Simple category selection holder
 */
public class EntityCategorySelectionHolder extends BaseEntitySelectionHolder {

    private final ImageView image;
    private final TextView name;
    private final ProgressBar imageBar;

    public EntityCategorySelectionHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(parent, layoutId, R.id.item_category_checkbox);
        image = (ImageView) itemView.findViewById(R.id.item_category_image);
        name = (TextView) itemView.findViewById(R.id.item_category_name);
        imageBar = (ProgressBar) itemView.findViewById(R.id.item_category_bar);
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Category category = (Category) entity;
        ImageLoader.get().source(category.getPhoto()).bar(imageBar).load(image);
        name.setText(category.getName());
    }
}
