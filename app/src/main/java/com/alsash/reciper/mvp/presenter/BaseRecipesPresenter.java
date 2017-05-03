package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

public abstract class BaseRecipesPresenter<V extends BaseRecipeListView> extends BasePresenter<V>
        implements RecipeListInteraction {

    @Override
    public void onExpand(Recipe recipe, int position) {
        if (getView() == null) return;
        getView().showDetails(recipe);
    }

    @Override
    public void onOpen(Recipe recipe, int position) {
        if (getView() == null) return;
        getView().showRecipe(recipe);
    }

}
