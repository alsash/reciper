package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsDescriptionPresenter
        extends BaseRestrictPresenter<RecipeDetailsDescriptionView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private RecipeFull recipeFull;

    private boolean descriptionEditable;

    public RecipeDetailsDescriptionPresenter(StorageLogic storageLogic,
                                             BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public RecipeDetailsDescriptionPresenter setRestriction(EntityRestriction restriction) {
        if (isNewRestriction(restriction)) {
            descriptionEditable = false;
        }
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
        view.setDescriptionEditable(descriptionEditable);
        view.showAuthor(recipeFull.getAuthor());
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

    public void requestAuthorEdit(RecipeDetailsDescriptionView view) {

    }

    public void requestDescriptionEdit(RecipeDetailsDescriptionView view) {
        descriptionEditable = !descriptionEditable;
        view.setDescriptionEditable(descriptionEditable);
        if (!descriptionEditable) {
            storageLogic.updateSync(recipeFull,
                    RecipeAction.EDIT_DESCRIPTION,
                    (Object[]) view.getDescriptionEditable());
            getComposite().add(Completable
                    .fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            storageLogic.updateAsync(recipeFull, RecipeAction.EDIT_DESCRIPTION);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }
    }
}
