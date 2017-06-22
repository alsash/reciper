package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.model.restriction.EntityRestriction;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsDescriptionPresenter
        extends BaseRestrictPresenter<RecipeDetailsDescriptionView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private EntityRestriction restriction;
    private RecipeFull recipeFull;
    private boolean recipeShown;

    public RecipeDetailsDescriptionPresenter(StorageLogic storageLogic,
                                             BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public RecipeDetailsDescriptionPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsDescriptionPresenter) super.setRestriction(restriction);
    }

    @Override
    public void visible(RecipeDetailsDescriptionView view) {

    }

    @Override
    public void invisible(RecipeDetailsDescriptionView view) {

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
