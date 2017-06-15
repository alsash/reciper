package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeListPresenter;
import com.alsash.reciper.mvp.view.RecipeListView;

/**
 * A Recipe group interaction, with factory of presenters for inner recipe lists
 */
public interface RecipeGroupInteraction<G extends BaseEntity> {

    BaseRecipeListPresenter<RecipeListView> injectInnerPresenter(G group);

    void onOpen(G group);

    void onOpen(Recipe recipe);
}
