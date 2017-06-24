package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
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
    private boolean animateNutrition;

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

    public void onNutritionSwitch(boolean switchOn, RecipeDetailsDescriptionView view) {
        recipeUnit = switchOn ? RecipeUnit.SERVING : RecipeUnit.GRAM;
        animateNutrition = true;
        visible(view);
        animateNutrition = false;
    }

    @Override
    public void visible(RecipeDetailsDescriptionView view) {
        view.showNutritionSwitch(recipeUnit.getDefaultQuantity(), recipeUnit);
        if (recipeFull != null)
            view.showNutritionValue(businessLogic.getNutrient(recipeFull, recipeUnit), animateNutrition);
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
