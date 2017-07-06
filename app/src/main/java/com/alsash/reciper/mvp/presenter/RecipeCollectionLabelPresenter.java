package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.event.LabelEvent;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionLabelView;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Notification;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * A Presenter that represents collection of a Recipes grouped by Labels
 */
public class RecipeCollectionLabelPresenter
        extends BaseRecipeGroupPresenter<Label, RecipeCollectionLabelView> {

    private static final int PAGINATION_LABEL_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    public RecipeCollectionLabelPresenter(StorageLogic storageLogic,
                                          BusinessLogic businessLogic) {
        super(PAGINATION_LABEL_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public void attach(RecipeCollectionLabelView view) {
        super.attach(view);
        final WeakReference<RecipeCollectionLabelView> viewRef = new WeakReference<>(view);
        getComposite().add(
                businessLogic
                        .getLabelEventSubject()
                        .doOnEach(new Consumer<Notification<LabelEvent>>() {
                            @Override
                            public void accept(@NonNull Notification<LabelEvent> notification)
                                    throws Exception {
                                LabelEvent event = notification.getValue();
                                if (event == null) return;
                                switch (event.action) {
                                    case CREATE:
                                    case DELETE:
                                        getModels().clear();
                                        resetPreviousPosition();
                                        setFetched(false);
                                        setLoading(false);
                                        if (viewRef.get() != null) {
                                            viewRef.get().setContainer(getModels());
                                            viewRef.get().setPagination(!isFetched());
                                            fetch(viewRef);
                                        }
                                        break;
                                    case EDIT:
                                        Integer editPosition = getPosition(event.uuid);
                                        if (editPosition == null) return;
                                        if (viewRef.get() != null)
                                            viewRef.get().showUpdate(editPosition);
                                        break;
                                }
                            }
                        })
                        .subscribe());
    }

    @Override
    protected StorageLogic getStorageLogic() {
        return storageLogic;
    }

    @Override
    protected BusinessLogic getBusinessLogic() {
        return businessLogic;
    }

    @Override
    protected List<Label> loadNextGroups(int offset, int limit) {
        return storageLogic.getLabels(offset, limit);
    }

    @Override
    public List<Recipe> loadNextRecipes(Label label, int offset, int limit) {
        return storageLogic.getRecipes(label, offset, limit);
    }
}
