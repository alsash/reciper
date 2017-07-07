package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeDialogCategoryView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Category selection dialog
 */
public class RecipeDialogCategoryPresenter
        extends BaseSelectionDialogPresenter<Category, RecipeDialogCategoryView> {

    private static final int PAGINATION_LIMIT = 10;
    private static final boolean SELECTION_SINGLE = false;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;
    private final Set<String> categoryUuid = new HashSet<>();

    public RecipeDialogCategoryPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, SELECTION_SINGLE, storageLogic);
        this.businessLogic = businessLogic;
        this.storageLogic = storageLogic;
    }

    @Override
    public RecipeDialogCategoryPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDialogCategoryPresenter) super.setRestriction(restriction);
    }

    @Override
    protected List<Category> loadNext(int offset, int limit) {
        return storageLogic.getCategories(offset, limit);
    }

    @Override
    protected Set<String> getRestrictedUuid(BaseEntity entity) {
        categoryUuid.clear();
        if (entity == null) return categoryUuid;
        categoryUuid.add(((Recipe) entity).getCategory().getUuid());
        return categoryUuid;
    }

    @Override
    protected boolean updateDbOnApproved() {
        if (getRestrictedEntity() == null) return false;
        Recipe recipe = (Recipe) getRestrictedEntity();
        String categoryUuid = getCategoryUuid();
        if (categoryUuid == null) return false;
        storageLogic.updateSync(recipe, RecipeAction.EDIT_CATEGORY, categoryUuid);
        storageLogic.updateAsync(recipe, RecipeAction.EDIT_CATEGORY);
        return true;
    }

    @Override
    protected void updateUiOnApproved(boolean dbUpdated) {
        if (!dbUpdated || getRestrictedEntity() == null) return;
        businessLogic.getRecipeEventSubject().onNext(new RecipeEvent(RecipeAction.EDIT_CATEGORY,
                getRestrictedEntity().getUuid()));
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private String getCategoryUuid() {
        for (String uuid : getSelectionUuid()) return uuid;
        return null;
    }
}
