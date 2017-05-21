package com.alsash.reciper.ui.fragment;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

/**
 * A simple fragment, that implements RecipeListInteractions
 */
public abstract class BaseRecipeListFragment<M, V extends BaseListView<M>>
        extends BaseListFragment<M, V>
        implements RecipeListInteraction {

    protected abstract AppNavigator getNavigator();

    @Override
    public void onExpand(Recipe recipe) {
        getNavigator().toRecipeExpandView(recipe.getId(),
                getActivity().getSupportFragmentManager());
    }

    @Override
    public void onOpen(Recipe recipe) {
        getNavigator().toRecipeMainView(recipe.getId());
    }
}
