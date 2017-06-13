package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupInnerPresenter;

/**
 * A Recipe group interaction, with factory of presenters for inner recipe lists
 */
public interface RecipeGroupInteraction<G extends BaseEntity> {

    BaseRecipeGroupInnerPresenter<G> injectInnerPresenter(G group);

    void onOpen(G group);
}
