package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.mvp.view.RecipeCreationDialogView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Composite presenter
 */
public class RecipeCreationDialogPresenter implements BasePresenter<RecipeCreationDialogView> {

    private static final int PAGINATION_LIMIT = 10;

    private final BusinessLogic businessLogic;
    private final StorageLogic storageLogic;

    private final BaseListPresenter<Author, BaseListView<Author>> authorsPresenter;
    private final BaseListPresenter<Category, BaseListView<Category>> categoriesPresenter;

    private Author selectedAuthor;
    private Category selectedCategory;

    public RecipeCreationDialogPresenter(StorageLogic sLogic, BusinessLogic bLogic) {
        businessLogic = bLogic;
        storageLogic = sLogic;
        authorsPresenter = new BaseListPresenter<Author, BaseListView<Author>>(PAGINATION_LIMIT) {
            @Override
            protected List<Author> loadNext(int offset, int limit) {
                return storageLogic.getAuthors(offset, limit);
            }
        };
        categoriesPresenter
                = new BaseListPresenter<Category, BaseListView<Category>>(PAGINATION_LIMIT) {
            @Override
            protected List<Category> loadNext(int offset, int limit) {
                return storageLogic.getCategories(offset, limit);
            }
        };
    }

    public void onSelect(BaseEntity entity) {
        if (entity instanceof Category)
            selectedCategory = (Category) entity;
        if (entity instanceof Author)
            selectedAuthor = (Author) entity;
    }

    public void onCreationApprove(RecipeCreationDialogView view, final String recipeName) {
        if (selectedAuthor == null || selectedCategory == null) {
            view.showError();
            view.finishView();
            return;
        }
        final WeakReference<RecipeCreationDialogView> viewRef = new WeakReference<>(view);
        authorsPresenter.getComposite().add(Maybe
                .fromCallable(new Callable<Recipe>() {
                    @Override
                    public Recipe call() throws Exception {
                        return storageLogic.createRecipeAsync(
                                recipeName,
                                selectedAuthor,
                                selectedCategory);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Recipe>() {
                    @Override
                    public void accept(@NonNull Recipe recipe) throws Exception {
                        businessLogic.getRecipeEventSubject().onNext(
                                new RecipeEvent(RecipeAction.CREATE, recipe.getUuid()));
                        if (viewRef.get() != null) {
                            viewRef.get().openRecipe(recipe);
                            viewRef.get().finishView();
                        }
                    }
                })
        );
    }

    public void nextPagination(Class<?> entityClass, int lastPosition) {
        if (entityClass.equals(Author.class)) {
            authorsPresenter.nextPagination(lastPosition);
        } else if (entityClass.equals(Category.class)) {
            categoriesPresenter.nextPagination(lastPosition);
        }
    }

    @Override
    public void attach(RecipeCreationDialogView view) {
        authorsPresenter.attach(view.getAuthorsView());
        categoriesPresenter.attach(view.getCategoriesView());
    }

    @Override
    public void visible(RecipeCreationDialogView view) {
        authorsPresenter.visible(view.getAuthorsView());
        categoriesPresenter.visible(view.getCategoriesView());
    }

    @Override
    public void invisible(RecipeCreationDialogView view) {
        authorsPresenter.invisible(view.getAuthorsView());
        categoriesPresenter.invisible(view.getCategoriesView());
    }

    @Override
    public void refresh(RecipeCreationDialogView view) {
        authorsPresenter.refresh(view.getAuthorsView());
        categoriesPresenter.refresh(view.getCategoriesView());
    }

    @Override
    public void detach() {
        authorsPresenter.detach();
        categoriesPresenter.detach();
    }
}
