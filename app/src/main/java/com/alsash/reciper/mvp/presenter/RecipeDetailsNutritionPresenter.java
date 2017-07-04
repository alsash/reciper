package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.logic.unit.RecipeUnit;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsNutritionView;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsNutritionPresenter
        extends BaseRestrictPresenter<RecipeDetailsNutritionView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private RecipeFull recipeFull;
    private RecipeUnit recipeUnit;

    public RecipeDetailsNutritionPresenter(StorageLogic storageLogic,
                                           BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
        this.recipeUnit = RecipeUnit.GRAM;
    }

    @Override
    public RecipeDetailsNutritionPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsNutritionPresenter) super.setRestriction(restriction);
    }

    public void onNutritionSwitch(boolean switchOn, RecipeDetailsNutritionView view) {
        recipeUnit = switchOn ? RecipeUnit.SERVING : RecipeUnit.GRAM;
        view.showNutritionQuantity(recipeUnit.getDefaultQuantity(), recipeUnit);
        if (recipeFull == null) return;
        view.showNutritionChart(businessLogic.getNutrient(recipeFull, recipeUnit));
        view.showNutritionAnimation();
    }

    @Override
    public void visible(RecipeDetailsNutritionView view) {
        view.showNutritionQuantity(recipeUnit.getDefaultQuantity(), recipeUnit);
        if (recipeFull == null) return;
        view.showNutritionChart(businessLogic.getNutrient(recipeFull, recipeUnit));
    }

    @Override
    public void invisible(RecipeDetailsNutritionView view) {
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
