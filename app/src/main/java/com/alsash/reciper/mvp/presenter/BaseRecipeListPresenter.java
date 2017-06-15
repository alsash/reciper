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
                                if (notification.getValue() == null || viewRef.get() == null)
                                    return;
                                RecipeEvent event = notification.getValue();
                                switch (event.action) {
                                    case FAVORITE:
                                        Integer fromPosition = getPosition(event.uuid);
                                        if (fromPosition == null) return;
                                        viewRef.get().showUpdate(fromPosition);
                                        break;
                                }
                            }
                        })
                        .subscribe());
    }

    public void changeFavorite(final Recipe recipe) {
        RecipeEvent event = new RecipeEvent(RecipeAction.FAVORITE, recipe.getUuid());
        storageLogic.updateSync(recipe, event.action);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateAsync(recipe);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
        businessLogic.getRecipeEventSubject().onNext(event);
    }
}
