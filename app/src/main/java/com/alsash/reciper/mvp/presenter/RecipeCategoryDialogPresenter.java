package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeCategoryDialogView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Composite presenter
 */
public class RecipeCategoryDialogPresenter
        extends BaseListPresenter<Category, RecipeCategoryDialogView> {

    private static final int PAGINATION_LIMIT = 10;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;
    private EntityRestriction restriction;

    private Recipe recipe;
    private Category selectedCategory;

    public RecipeCategoryDialogPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    public RecipeCategoryDialogPresenter setRestriction(EntityRestriction restriction) {
        this.restriction = restriction;
        recipe = null;
        selectedCategory = null;
        detach();
        return this;
    }

    @Override
    public void attach(RecipeCategoryDialogView view) {
        super.attach(view);
        if (recipe != null) return;
        final WeakReference<RecipeCategoryDialogView> viewRef = new WeakReference<>(view);
        getComposite().add(Maybe
                .fromCallable(new Callable<BaseEntity>() {
                    @Override
                    public BaseEntity call() throws Exception {
                        return storageLogic.getRestrictEntity(restriction);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseEntity>() {
                    @Override
                    public void accept(@NonNull BaseEntity loadedEntity) throws Exception {
                        recipe = (RecipeFull) loadedEntity;
                        if (viewRef.get() != null && viewRef.get().isViewVisible())
                            viewRef.get().setSelected(recipe.getCategory());
                    }
                })
        );
    }

    public void onSelect(Category category) {
        selectedCategory = category;
    }

    public void onApprove(RecipeCategoryDialogView view) {
        if (selectedCategory == null
                || recipe == null
                || selectedCategory.getUuid().equals(recipe.getCategory().getUuid())) {
            view.finishView();
            return;
        }

        final WeakReference<RecipeCategoryDialogView> viewRef = new WeakReference<>(view);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateSync(recipe, RecipeAction.EDIT_CATEGORY,
                                selectedCategory.getUuid());
                        storageLogic.updateAsync(recipe, RecipeAction.EDIT_CATEGORY);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        businessLogic.getRecipeEventSubject().onNext(
                                new RecipeEvent(RecipeAction.EDIT_CATEGORY, recipe.getUuid()));
                        if (viewRef.get() != null)
                            viewRef.get().finishView();
                    }
                })
        );
    }

    @Override
    protected List<Category> loadNext(int offset, int limit) {
        return storageLogic.getCategories(offset, limit);
    }
}
