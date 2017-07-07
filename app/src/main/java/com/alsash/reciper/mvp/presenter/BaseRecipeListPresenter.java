package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.Notification;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A Recipe List Presenter, that implement Recipe actions logic
 */
public abstract class BaseRecipeListPresenter<V extends RecipeListView>
        extends BaseListPresenter<Recipe, V> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    public BaseRecipeListPresenter(int limit,
                                   StorageLogic storageLogic,
                                   BusinessLogic businessLogic) {
        super(limit);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public void attach(V view) {
        super.attach(view);
        final WeakReference<V> viewRef = new WeakReference<>(view);
        getComposite().add(
                businessLogic
                        .getRecipeEventSubject()
                        .doOnEach(new Consumer<Notification<RecipeEvent>>() {
                            @Override
                            public void accept(@NonNull Notification<RecipeEvent> notification)
                                    throws Exception {
                                RecipeEvent event = notification.getValue();
                                if (event == null) return;
                                switch (event.action) {
                                    case CREATE:
                                        clear(viewRef);
                                        break;
                                    case EDIT:
                                    case EDIT_NAME:
                                    case EDIT_PHOTO:
                                    case EDIT_AUTHOR:
                                    case EDIT_FAVORITE:
                                    case EDIT_DESCRIPTION:
                                        Integer editPosition = getPosition(event.uuid);
                                        if (editPosition == null) return;
                                        if (viewRef.get() != null)
                                            viewRef.get().showUpdate(editPosition);
                                        break;
                                    case DELETE:
                                        Integer deletePosition = getPosition(event.uuid);
                                        if (deletePosition == null) return;
                                        getModels().remove((int) deletePosition);
                                        if (viewRef.get() != null)
                                            viewRef.get().showDelete(deletePosition);
                                }
                            }
                        })
                        .subscribe());
    }

    public void changeFavorite(final Recipe recipe) {
        final RecipeEvent event = new RecipeEvent(RecipeAction.EDIT_FAVORITE, recipe.getUuid());
        storageLogic.updateSync(recipe, event.action, !recipe.isFavorite());
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateAsync(recipe, event.action);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
        businessLogic.getRecipeEventSubject().onNext(event);
    }
}
