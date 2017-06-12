package com.alsash.reciper.ui.adapter;

import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.ui.adapter.holder.RecipeCategoryCardHolder;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.List;

/**
 * An Adapter that makes Recipe Category view holders
 */
public class RecipeCategoryCardListAdapter
        extends BaseRecipeGroupAdapter<Category, RecipeCategoryCardHolder> {

    public RecipeCategoryCardListAdapter(List<Category> groupList,
                                         RecipeGroupInteraction<Category> groupInteraction,
                                         RecipeListInteraction recipeInteraction) {
        super(groupList, groupInteraction, recipeInteraction);
    }

    @Override
    public RecipeCategoryCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeCategoryCardHolder(parent);
    }
}
