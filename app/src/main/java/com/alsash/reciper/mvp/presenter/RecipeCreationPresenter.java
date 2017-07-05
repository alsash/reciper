package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.mvp.view.RecipeCreationView;

import java.util.List;

/**
 * Composite presenter
 */
public class RecipeCreationPresenter implements BasePresenter<RecipeCreationView> {

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

    public RecipeCreationPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
        this.storageLogic = storageLogic;
    }

    public void onSelect(BaseEntity entity) {
        if (entity instanceof Category)
            selectedCategory = (Category) entity;
        if (entity instanceof Author)
            selectedAuthor = (Author) entity;
    }

    public void onCreationApprove(RecipeCreationView view) {

    }

    public void nextPagination(Class<?> entityClass, int lastPosition) {
        if (entityClass.equals(Author.class)) {
            authorListPresenter.nextPagination(lastPosition);
        } else if (entityClass.equals(Category.class)) {
            categoryListPresenter.nextPagination(lastPosition);
        }
    }

    @Override
    public void attach(RecipeCreationView view) {
        authorListPresenter.attach(view.getAuthorsView());
        categoryListPresenter.attach(view.getCategoriesView());
    }

    @Override
    public void visible(RecipeCreationView view) {
        authorListPresenter.visible(view.getAuthorsView());
        categoryListPresenter.visible(view.getCategoriesView());
    }

    @Override
    public void invisible(RecipeCreationView view) {
        authorListPresenter.invisible(view.getAuthorsView());
        categoryListPresenter.invisible(view.getCategoriesView());
    }

    @Override
    public void refresh(RecipeCreationView view) {
        authorListPresenter.refresh(view.getAuthorsView());
        categoryListPresenter.refresh(view.getCategoriesView());
    }

    @Override
    public void detach() {
        authorListPresenter.detach();
        categoryListPresenter.detach();
    }
}
