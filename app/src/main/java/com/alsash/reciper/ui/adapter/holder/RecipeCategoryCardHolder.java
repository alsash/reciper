package com.alsash.reciper.ui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * Simple view holder that holds Category
 * and shows list of related Recipes with help of their Presenter
 */
public class RecipeCategoryCardHolder extends BaseRecipeGroupHolder<Category> {

    private final ImageView image;
    private final TextView name;
    private final ProgressBar bar;
    private final MutableBoolean barFree = new MutableBoolean().set(false);

    public RecipeCategoryCardHolder(ViewGroup parent) {
        super(parent, R.layout.item_category_group, R.id.item_category_inner_list);
        image = (ImageView) itemView.findViewById(R.id.item_category_image);
        name = (TextView) itemView.findViewById(R.id.item_category_name);
        bar = (ProgressBar) itemView.findViewById(R.id.item_category_bar);
    }

    @Override
    protected int getRecipeCardLayout() {
        return R.layout.item_recipe_card_category;
    }

    @Override
    public void bindGroup(Category category) {
        ImageLoader.get().source(category.getPhoto()).bar(bar).completer(barFree).load(image);
        name.setText(category.getName());
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. openListener
     */
    @Override
    public void setListeners(View.OnClickListener... listeners) {
        name.setOnClickListener(listeners[0]);
    }

    @Override
    public void showLoading(boolean loading) {
        if (barFree.is()) bar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
