package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeDialogAuthorView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author selection dialog
 */
public class RecipeDialogAuthorPresenter
        extends BaseSelectionDialogPresenter<Author, RecipeDialogAuthorView> {

    private static final int PAGINATION_LIMIT = 10;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;
    private final Set<String> authorUuid = new HashSet<>();

    public RecipeDialogAuthorPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, SELECTION_SINGLE, storageLogic);
        this.businessLogic = businessLogic;
        this.storageLogic = storageLogic;
    }

    @Override
    public RecipeDialogAuthorPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDialogAuthorPresenter) super.setRestriction(restriction);
    }

    @Override
    protected List<Author> loadNext(int offset, int limit) {
        return storageLogic.getAuthors(offset, limit);
    }

    @Override
    protected Set<String> getRestrictedUuid(BaseEntity entity) {
        authorUuid.clear();
        if (entity == null) return authorUuid;
        authorUuid.add(((Recipe) entity).getAuthor().getUuid());
        return authorUuid;
    }

    @Override
    protected boolean updateDbOnApproved() {
        if (getRestrictedEntity() == null) return false;
        Recipe recipe = (Recipe) getRestrictedEntity();
        String authorUuid = getAuthorUuid();
        if (authorUuid == null) return false;
        storageLogic.updateSync(recipe, RecipeAction.EDIT_AUTHOR, authorUuid);
        storageLogic.updateAsync(recipe, RecipeAction.EDIT_AUTHOR);
        return true;
    }

    @Override
    protected void updateUiOnApproved(boolean dbUpdated) {
        if (!dbUpdated || getRestrictedEntity() == null) return;
        businessLogic.getRecipeEventSubject().onNext(new RecipeEvent(RecipeAction.EDIT_AUTHOR,
                getRestrictedEntity().getUuid()));
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private String getAuthorUuid() {
        for (String uuid : getSelectionUuid()) return uuid;
        return null;
    }
}
