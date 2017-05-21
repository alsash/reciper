package com.alsash.reciper.ui.adapter;

import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupListPresenter;
import com.alsash.reciper.ui.adapter.holder.RecipeLabelCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.List;

/**
 * An Adapter that makes Recipe Label view holders
 */
public class RecipeLabelCardListAdapter
        extends BaseRecipeGroupAdapter<Label, RecipeLabelCardHolder> {

    public RecipeLabelCardListAdapter(List<Label> groupList,
                                      RecipeGroupInteraction groupInteraction,
                                      RecipeListInteraction recipeInteraction) {
        super(groupList, groupInteraction, recipeInteraction);
    }

    @Override
    protected RecipeLabelCardHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType,
                                                       BaseRecipeGroupListPresenter presenter,
                                                       RecipeListInteraction recipeInteraction) {
        return new RecipeLabelCardHolder(parent, presenter, recipeInteraction);
    }
}
