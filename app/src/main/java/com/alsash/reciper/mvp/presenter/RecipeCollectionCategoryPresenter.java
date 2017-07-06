package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.event.CategoryEvent;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionCategoryView;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Notification;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * A Presenter that represents collection of Recipes grouped by Categories
 */
public class RecipeCollectionCategoryPresenter
        extends BaseRecipeGroupPresenter<Category, RecipeCollectionCategoryView> {

    private static final int PAGINATION_CATEGORY_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    public RecipeCollectionCategoryPresenter(StorageLogic storageLogic,
                                             BusinessLogic businessLogic) {
        super(PAGINATION_CATEGORY_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public void attach(RecipeCollectionCategoryView view) {
        super.attach(view);
        final WeakReference<RecipeCollectionCategoryView> viewRef = new WeakReference<>(view);
        getComposite().add(
                businessLogic
                        .getCategoryEventSubject()
                        .doOnEach(new Consumer<Notification<CategoryEvent>>() {
                            @Override
                            public void accept(@NonNull Notification<CategoryEvent> notification)
                                    throws Exception {
                                CategoryEvent event = notification.getValue();
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
    protected List<Category> loadNextGroups(int offset, int limit) {
        return storageLogic.getCategories(offset, limit);
    }

    @Override
    public List<Recipe> loadNextRecipes(Category category, int offset, int limit) {
        return storageLogic.getRecipes(category, offset, limit);
    }
}
