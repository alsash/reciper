package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeAuthorDialogView;

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
public class RecipeAuthorDialogPresenter
        extends BaseListPresenter<Author, RecipeAuthorDialogView> {

    private static final int PAGINATION_LIMIT = 10;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;
    private EntityRestriction restriction;

    private Recipe recipe;
    private Author selectedAuthor;

    public RecipeAuthorDialogPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    public RecipeAuthorDialogPresenter setRestriction(EntityRestriction restriction) {
        this.restriction = restriction;
        recipe = null;
        selectedAuthor = null;
        detach();
        return this;
    }

    @Override
    public void attach(RecipeAuthorDialogView view) {
        super.attach(view);
        if (recipe != null) return;
        final WeakReference<RecipeAuthorDialogView> viewRef = new WeakReference<>(view);
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
                            viewRef.get().setSelected(recipe.getAuthor());
                    }
                })
        );
    }

    public void onSelect(Author author) {
        selectedAuthor = author;
    }

    public void onApprove(RecipeAuthorDialogView view) {
        if (selectedAuthor == null
                || recipe == null
                || selectedAuthor.getUuid().equals(recipe.getAuthor().getUuid())) {
            view.finishView();
            return;
        }

        final WeakReference<RecipeAuthorDialogView> viewRef = new WeakReference<>(view);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateSync(recipe, RecipeAction.EDIT_AUTHOR,
                                selectedAuthor.getUuid());
                        storageLogic.updateAsync(recipe, RecipeAction.EDIT_AUTHOR);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        businessLogic.getRecipeEventSubject().onNext(
                                new RecipeEvent(RecipeAction.EDIT_AUTHOR, recipe.getUuid()));
                        if (viewRef.get() != null)
                            viewRef.get().finishView();
                    }
                })
        );
    }

    @Override
    protected List<Author> loadNext(int offset, int limit) {
        return storageLogic.getAuthors(offset, limit);
    }
}
