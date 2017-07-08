package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeDialogIngredientView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Food selection dialog
 */
public class RecipeDialogIngredientPresenter
        extends BaseSelectionDialogPresenter<Food, RecipeDialogIngredientView> {

    private static final int PAGINATION_LIMIT = 20;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;
    private final Set<String> foodUuid = new HashSet<>();

    public RecipeDialogIngredientPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, SELECTION_SINGLE, storageLogic);
        this.businessLogic = businessLogic;
        this.storageLogic = storageLogic;
    }

    @Override
    public RecipeDialogIngredientPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDialogIngredientPresenter) super.setRestriction(restriction);
    }

    @Override
    protected List<Food> loadNext(int offset, int limit) {
        return storageLogic.getFoods(offset, limit);
    }

    @Override
    protected Set<String> getRestrictedUuid(BaseEntity entity) {
        return foodUuid; // empty
    }

    @Override
    protected boolean updateDbOnApproved() {
        if (getRestrictedEntity() == null) return false;
        Recipe recipe = (Recipe) getRestrictedEntity();
        String foodUuid = getFoodUuid();
        if (foodUuid == null) return false;
        storageLogic.updateAsync(recipe, RecipeAction.CREATE_INGREDIENT, getSelectionUuid());
        return true;
    }

    @Override
    protected void updateUiOnApproved(boolean dbUpdated) {
        if (!dbUpdated || getRestrictedEntity() == null) return;
        businessLogic.getRecipeEventSubject().onNext(new RecipeEvent(RecipeAction.CREATE_INGREDIENT,
                getRestrictedEntity().getUuid()));
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private String getFoodUuid() {
        for (String uuid : getSelectionUuid()) return uuid;
        return null;
    }
}
