package com.alsash.reciper.ui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Label;

/**
 * Simple view holder that holds Label
 * and shows list of related Recipes with help of their Presenter
 */
public class RecipeLabelCardHolder extends BaseRecipeGroupHolder<Label> {

    private final ImageView image;
    private final TextView name;
    private final ProgressBar bar;

    public RecipeLabelCardHolder(ViewGroup parent) {
        super(parent, R.layout.item_label_group, R.id.item_label_inner_list);
        image = (ImageView) itemView.findViewById(R.id.item_label_image);
        name = (TextView) itemView.findViewById(R.id.item_label_name);
        bar = (ProgressBar) itemView.findViewById(R.id.item_label_bar);
    }

    @Override
    protected int getRecipeCardLayout() {
        return R.layout.item_recipe_card_label;
    }

    @Override
    public void bindGroup(Label label) {
        name.setText(label.getName());
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. openListener
     */
    @Override
    public void setListeners(View.OnClickListener... listeners) {
        image.setOnClickListener(listeners[0]);
        name.setOnClickListener(listeners[0]);
    }

    @Override
    public void showLoading(boolean loading) {
        bar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
