package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsDescriptionPresenter
        extends BaseRestrictPresenter<RecipeDetailsDescriptionView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private RecipeFull recipeFull;
    private RecipeUnit recipeUnit;

    public RecipeDetailsDescriptionPresenter(StorageLogic storageLogic,
                                             BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
        this.recipeUnit = RecipeUnit.GRAM;
    }

    @Override
    public RecipeDetailsDescriptionPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsDescriptionPresenter) super.setRestriction(restriction);
    }

    public void deleteLabel(Label label) {

    }

    public void addLabel() {

    }

    @Override
    public void visible(RecipeDetailsDescriptionView view) {
        if (recipeFull == null) return;
        view.showDescription(recipeFull);
        view.showCategory(recipeFull.getCategory());
        view.showLabels(recipeFull.getLabels());
        view.showCookTime(businessLogic.getCookTime(recipeFull));
    }

    @Override
    public void invisible(RecipeDetailsDescriptionView view) {
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
