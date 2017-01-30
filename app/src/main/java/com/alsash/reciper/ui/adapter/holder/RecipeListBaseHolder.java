package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.RecipeListAdapter;

import java.util.List;

public abstract class RecipeListBaseHolder extends RecyclerView.ViewHolder {

    public RecipeListBaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindRecipe(Recipe recipe);

    public abstract List<RecipeListAdapter.ActionViewEntry> getActionViews();
}
