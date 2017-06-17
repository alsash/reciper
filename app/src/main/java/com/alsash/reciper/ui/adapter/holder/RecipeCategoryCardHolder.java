package com.alsash.reciper.ui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.util.MutableBoolean;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.ui.loader.ImageLoader;

/**
 * Simple view holder that holds Category
 * and shows list of related Recipes with help of their Presenter
 */
public class RecipeCategoryCardHolder extends BaseRecipeGroupHolder<Category> {

    private final ImageView imageBackground;
    private final TextView nameButton;
    private final ProgressBar bar;
    private final MutableBoolean barFree = new MutableBoolean().set(false);

    public RecipeCategoryCardHolder(ViewGroup parent) {
        super(parent, R.layout.item_category_group, R.id.item_category_inner_list);
        imageBackground = (ImageView) itemView.findViewById(R.id.item_category_image);
        nameButton = (TextView) itemView.findViewById(R.id.item_category_name);
        bar = (ProgressBar) itemView.findViewById(R.id.item_category_bar);
    }

    @Override
    protected int getRecipeCardLayout() {
        return R.layout.item_category_recipe_card;
    }

    @Override
    public void bindGroup(Category category) {
        ImageLoader.get()
                .source(category.getPhoto())
                .bar(bar)
                .completer(barFree)
                .load(imageBackground);
        nameButton.setText(category.getName());
    }

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. openListener
     */
    @Override
    public void setListeners(View.OnClickListener... listeners) {
        nameButton.setOnClickListener(listeners[0]);
    }

    @Override
    public void showLoading(boolean loading) {
        if (barFree.is()) bar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
