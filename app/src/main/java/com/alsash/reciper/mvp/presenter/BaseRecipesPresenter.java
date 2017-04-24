package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.mvp.view.BaseRecipesView;

public abstract class BaseRecipesPresenter<V extends BaseRecipesView> extends BasePresenter<V>
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
