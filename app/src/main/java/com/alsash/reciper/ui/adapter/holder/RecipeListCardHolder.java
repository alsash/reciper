package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alsash.reciper.data.model.Recipe;

public abstract class RecipeListCardHolder extends RecyclerView.ViewHolder {

    RecipeListCardHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindRecipe(Recipe recipe);

    /**
     * Set the listeners in the following sequence:
     *
     * @param listeners 0. flipListener
     *                  1. expandListener
     *                  2. openListener
     */
    public abstract void setListeners(View.OnClickListener... listeners);
}
