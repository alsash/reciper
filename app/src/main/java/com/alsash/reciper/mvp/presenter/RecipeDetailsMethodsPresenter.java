package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsMethodsView;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsMethodsPresenter extends BaseRestrictPresenter<RecipeDetailsMethodsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private RecipeFull recipeFull;

    public RecipeDetailsMethodsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public RecipeDetailsMethodsPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsMethodsPresenter) super.setRestriction(restriction);
    }

    @Override
    public void visible(RecipeDetailsMethodsView view) {
        if (recipeFull == null) return;
        view.showMethods(recipeFull.getMethods());
    }

    @Override
    public void invisible(RecipeDetailsMethodsView view) {
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
