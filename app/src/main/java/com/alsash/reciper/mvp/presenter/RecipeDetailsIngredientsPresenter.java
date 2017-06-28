package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsIngredientsView;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsIngredientsPresenter extends BaseRestrictPresenter<RecipeDetailsIngredientsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private RecipeFull recipeFull;

    public RecipeDetailsIngredientsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public RecipeDetailsIngredientsPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsIngredientsPresenter) super.setRestriction(restriction);
    }

    @Override
    public void visible(RecipeDetailsIngredientsView view) {
        if (recipeFull == null) return;
        view.showIngredients(recipeFull.getIngredients());
    }

    @Override
    public void invisible(RecipeDetailsIngredientsView view) {
        // do nothing
    }

    @Nullable
    @Override
    protected BaseEntity getEntity() {
        return recipeFull;
    }

    @Override
    protected void setEntity(@Nullable BaseEntity entity) {
        recipeFull = (RecipeFull) entity;
    }
}
