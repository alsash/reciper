package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDialogLabelView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A Label selection dialog
 */
public class RecipeDialogLabelPresenter
        extends BaseSelectionDialogPresenter<Label, RecipeDialogLabelView> {

    private static final int PAGINATION_LIMIT = 20;
    private static final boolean SELECTION_MULTI = true;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;

    public RecipeDialogLabelPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, SELECTION_MULTI, storageLogic);
        this.businessLogic = businessLogic;
        this.storageLogic = storageLogic;
    }

    @Override
    public RecipeDialogLabelPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDialogLabelPresenter) super.setRestriction(restriction);
    }

    @Override
    protected List<Label> loadNext(int offset, int limit) {
        return storageLogic.getLabels(offset, limit);
    }

    @Override
    protected Set<String> getRestrictedUuid(BaseEntity entity) {
        Set<String> labelUuid = new LinkedHashSet<>();
        if (entity == null) return labelUuid;
        for (Label label : ((RecipeFull) entity).getLabels()) {
            labelUuid.add(label.getUuid());
        }
        return labelUuid;
    }

    @Override
    protected boolean updateDbOnApproved() {
        if (getRestrictedEntity() == null) return false;
        RecipeFull recipe = (RecipeFull) getRestrictedEntity();
        storageLogic.updateAsync(recipe, RecipeAction.EDIT_LABELS, getSelectionUuid());
        return true;
    }

    @Override
    protected void updateUiOnApproved(boolean dbUpdated) {
        if (!dbUpdated || getRestrictedEntity() == null) return;
        businessLogic.getRecipeEventSubject().onNext(new RecipeEvent(RecipeAction.EDIT_LABELS,
                getRestrictedEntity().getUuid()));
    }
}
