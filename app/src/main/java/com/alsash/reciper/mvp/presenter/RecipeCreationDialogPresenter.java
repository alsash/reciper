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

    private BusinessLogic businessLogic;
    private StorageLogic storageLogic;

    private Author selectedAuthor;
    private Category selectedCategory;

    private BaseListPresenter<Author, BaseListView<Author>> authorListPresenter
            = new BaseListPresenter<Author, BaseListView<Author>>(PAGINATION_LIMIT) {
        @Override
        protected List<Author> loadNext(int offset, int limit) {
            return storageLogic.getAuthors(offset, limit);
        }
    };

    private BaseListPresenter<Category, BaseListView<Category>> categoryListPresenter
            = new BaseListPresenter<Category, BaseListView<Category>>(PAGINATION_LIMIT) {
        @Override
        protected List<Category> loadNext(int offset, int limit) {
            return storageLogic.getCategories(offset, limit);
        }
    };

    public RecipeCreationDialogPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
        this.storageLogic = storageLogic;
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
        authorListPresenter.getComposite().add(Maybe
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
            authorListPresenter.nextPagination(lastPosition);
        } else if (entityClass.equals(Category.class)) {
            categoryListPresenter.nextPagination(lastPosition);
        }
    }

    @Override
    public void attach(RecipeCreationDialogView view) {
        authorListPresenter.attach(view.getAuthorsView());
        categoryListPresenter.attach(view.getCategoriesView());
    }

    @Override
    public void visible(RecipeCreationDialogView view) {
        authorListPresenter.visible(view.getAuthorsView());
        categoryListPresenter.visible(view.getCategoriesView());
    }

    @Override
    public void invisible(RecipeCreationDialogView view) {
        authorListPresenter.invisible(view.getAuthorsView());
        categoryListPresenter.invisible(view.getCategoriesView());
    }

    @Override
    public void refresh(RecipeCreationDialogView view) {
        authorListPresenter.refresh(view.getAuthorsView());
        categoryListPresenter.refresh(view.getCategoriesView());
    }

    @Override
    public void detach() {
        authorListPresenter.detach();
        categoryListPresenter.detach();
    }
}
