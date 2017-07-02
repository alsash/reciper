package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.view.EntityListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A Presenter that represents collection of all entities (except recipes)
 */
public class EntityListPresenter extends BaseListPresenter<BaseEntity, EntityListView> {

    private static final int PAGINATION_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private EntityRestriction restriction;

    public EntityListPresenter(StorageLogic storageLogic,
                               BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    public EntityListPresenter setEntityRestriction(EntityRestriction restriction) {
        if (this.restriction != null) refresh();
        this.restriction = restriction;
        return this;
    }

    @Override
    public void attach(EntityListView view) {
        view.setEntityClass(restriction.entityClass);
        super.attach(view);
    }

    public void editValues(EntityListView view, BaseEntity entity, String... values) {

    }

    public void addEntity(EntityListView view) {

    }

    public void deleteEntity(EntityListView view, BaseEntity entity) {

    }

    @Override
    protected List<BaseEntity> loadNext(int offset, int limit) {

        List<BaseEntity> entityList = new ArrayList<>();

        if (restriction.entityClass.equals(Category.class))
            entityList.addAll(storageLogic.getCategories(offset, limit));

        if (restriction.entityClass.equals(Label.class))
            entityList.addAll(storageLogic.getLabels(offset, limit));

        if (restriction.entityClass.equals(Food.class))
            entityList.addAll(storageLogic.getFoods(offset, limit));

        if (restriction.entityClass.equals(Author.class))
            entityList.addAll(storageLogic.getAuthors(offset, limit));

        return entityList;
    }
}
