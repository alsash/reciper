package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.app.lib.MutableLong;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Notification;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A Presenter for its view
 */
public class RecipeDetailsDescriptionPresenter
        extends BaseRestrictPresenter<RecipeDetailsDescriptionView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private RecipeFull recipeFull;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<Object> listeners = new ArrayList<>();

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

    @Override
    public void attach(RecipeDetailsDescriptionView view) {
        super.attach(view);
        final WeakReference<RecipeDetailsDescriptionView> viewRef = new WeakReference<>(view);
        getComposite().add(
                businessLogic
                        .getRecipeEventSubject()
                        .doOnEach(new Consumer<Notification<RecipeEvent>>() {
                            @Override
                            public void accept(@NonNull Notification<RecipeEvent> notification)
                                    throws Exception {
                                RecipeEvent event = notification.getValue();
                                if (event == null) return;
                                // Updates from dialog...
                                switch (event.action) {
                                    case EDIT_CATEGORY:
                                        if (viewRef.get() != null
                                                && viewRef.get().isViewVisible()
                                                && recipeFull != null)
                                            viewRef.get().showCategory(recipeFull.getCategory());
                                        break;
                                    case EDIT_AUTHOR:
                                        if (viewRef.get() != null
                                                && viewRef.get().isViewVisible()
                                                && recipeFull != null)
                                            viewRef.get().showAuthor(recipeFull.getAuthor());
                                        break;
                                    case EDIT_LABELS:
                                        if (viewRef.get() != null
                                                && viewRef.get().isViewVisible()
                                                && recipeFull != null)
                                            viewRef.get().showLabels(recipeFull.getLabels());
                                        break;
                                }
                            }
                        }).subscribe());
    }

    @Override
    public void visible(RecipeDetailsDescriptionView view) {
        view.setDescriptionEditable(descriptionEditable);
        if (recipeFull == null) return;
        view.showDescription(recipeFull);
        view.showAuthor(recipeFull.getAuthor());
        view.showCategory(recipeFull.getCategory());
        view.showLabels(recipeFull.getLabels());
        view.showCookTime(businessLogic.getCookTime(recipeFull));
    }

    @Override
    public void invisible(RecipeDetailsDescriptionView view) {
        // do nothing
    }

    @Override
    public void detach() {
        super.detach();
        listeners.clear();
        listeners = new ArrayList<>();
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

    public void requestAuthorEdit(RecipeDetailsDescriptionView view) {
        if (recipeFull != null)
            view.showAuthorEditDialog(recipeFull);
    }

    public void requestCategoryEdit(RecipeDetailsDescriptionView view) {
        if (recipeFull != null)
            view.showCategoryEditDialog(recipeFull);
    }

    public void requestLabelsEdit(RecipeDetailsDescriptionView view) {
        if (recipeFull != null)
            view.showLabelEditDialog(recipeFull);
    }

    public void requestRecipeTime(RecipeDetailsDescriptionView view) {
        if (recipeFull == null) return;

        final WeakReference<RecipeDetailsDescriptionView> viewRef = new WeakReference<>(view);

        long cookTime = businessLogic.getCookTime(recipeFull);
        final MutableLong listener = new MutableLong(cookTime) {
            @Override
            public synchronized MutableLong set(long value) {
                storageLogic.updateSync(
                        recipeFull,
                        RecipeAction.EDIT_TIME,
                        businessLogic.getMassFlowRate(recipeFull, value)
                );
                if (viewRef.get() != null)
                    viewRef.get().showCookTime(businessLogic.getCookTime(recipeFull));
                getComposite().add(Completable
                        .fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                storageLogic.updateAsync(recipeFull, RecipeAction.EDIT_TIME);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .subscribe());
                return super.set(value);
            }
        };
        listeners.add(listener);
        view.showCookTimeEditDialog(listener);
    }

}
