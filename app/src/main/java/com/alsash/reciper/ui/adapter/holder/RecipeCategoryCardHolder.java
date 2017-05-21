package com.alsash.reciper.ui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupListPresenter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

/**
 * Simple view holder that holds Category
 * and shows list of related Recipes with help of their Presenter
 */
public class RecipeCategoryCardHolder extends BaseRecipeGroupHolder<Category> {

    private TextView categoryName;
    private ProgressBar progressBar;

    public RecipeCategoryCardHolder(ViewGroup parent,
                                    BaseRecipeGroupListPresenter presenter,
                                    RecipeListInteraction interaction) {
        super(parent, R.layout.view_list_group, R.id.group_list, presenter, interaction);
        progressBar = (ProgressBar) itemView.findViewById(R.id.group_progress);
        categoryName = (TextView) itemView.findViewById(R.id.group_name);
    }

    @Override
    public void bindGroup(Category category) {
        categoryName.setText(category.getName());
        super.bindGroup(category);
    }

    @Override
    public void showLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
