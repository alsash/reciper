package com.alsash.reciper.ui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupListPresenter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

/**
 * Simple view holder that holds Label
 * and shows list of related Recipes with help of their Presenter
 */
public class RecipeLabelCardHolder extends BaseRecipeGroupHolder<Label> {

    private TextView labelName;
    private ProgressBar progressBar;

    public RecipeLabelCardHolder(ViewGroup parent,
                                 BaseRecipeGroupListPresenter presenter,
                                 RecipeListInteraction interaction) {
        super(parent, R.layout.view_list_group, R.id.group_list, presenter, interaction);
        progressBar = (ProgressBar) itemView.findViewById(R.id.group_progress);
        labelName = (TextView) itemView.findViewById(R.id.group_name);
    }

    @Override
    public void bindGroup(Label label) {
        labelName.setText(label.getName());
        super.bindGroup(label);
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
