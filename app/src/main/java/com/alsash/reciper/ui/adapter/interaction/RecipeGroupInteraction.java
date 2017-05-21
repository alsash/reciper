package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.presenter.BaseRecipeGroupListPresenter;

/**
 * A Recipe group interaction, with factory of presenters for inner recipe lists
 */
public interface RecipeGroupInteraction {

    BaseRecipeGroupListPresenter getInnerPresenter();
}
