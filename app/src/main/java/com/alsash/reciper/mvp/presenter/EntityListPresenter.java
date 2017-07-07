package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.CategoryAction;
import com.alsash.reciper.logic.action.LabelAction;
import com.alsash.reciper.logic.event.CategoryEvent;
import com.alsash.reciper.logic.event.LabelEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.view.EntityListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
 * A Presenter that represents collection of all entities (except recipes)
 */
public class EntityListPresenter extends BaseListPresenter<BaseEntity, EntityListView> {

    private static final int PAGINATION_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private EntityRestriction restriction;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<MutableBoolean> deleteListeners = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<MutableString> editListeners = new ArrayList<>();

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

    public void onOpenEntity(EntityListView view, final BaseEntity entity) {

        final WeakReference<EntityListView> viewRef = new WeakReference<>(view);

        getComposite().add(Maybe.fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return storageLogic.hasRelatedRecipes(entity);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean has) throws Exception {
                                if (has) viewRef.get().showRelation(entity);
                            }
                        })

        );
    }

    @Override
    public void detach() {
        super.detach();
        deleteListeners.clear();
        deleteListeners = new ArrayList<>();
        editListeners.clear();
        editListeners = new ArrayList<>();
    }

    public void addEntity(EntityListView view) {
        final WeakReference<EntityListView> viewRef = new WeakReference<>(view);

        getComposite().add(Maybe
                .fromCallable(new Callable<BaseEntity>() {
                    @Override
                    public BaseEntity call() throws Exception {
                        return storageLogic.createAsync(restriction.entityClass);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseEntity>() {
                    @Override
                    public void accept(@NonNull BaseEntity entity) throws Exception {
                        getModels().add(0, entity);
                        if (viewRef.get() != null) viewRef.get().showInsert(0);
                        if (entity instanceof Category)
                            businessLogic
                                    .getCategoryEventSubject()
                                    .onNext(new CategoryEvent(
                                            CategoryAction.CREATE, entity.getUuid()));
                        if (entity instanceof Label)
                            businessLogic
                                    .getLabelEventSubject()
                                    .onNext(new LabelEvent(
                                            LabelAction.CREATE, entity.getUuid()));
                    }
                })
        );
    }

    public void deleteEntity(final EntityListView view, final int position) {

        // Instant remove entity
        final BaseEntity entity = getModels().remove(position);
        if (entity == null) return;
        view.showDelete(position);

        final String entityName = businessLogic.getEntityName(entity);
        final WeakReference<EntityListView> viewRef = new WeakReference<>(view);
        final MutableBoolean rejectCallback = getDeleteRejectCallback(viewRef, entity, position);

        // Check can entity be deleted
        getComposite().add(Maybe
                .fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return storageLogic.getRelatedRecipesSize(entity);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer recipesCount) throws Exception {
                        if (viewRef.get() == null) return;
                        if (recipesCount == 0) {
                            // Delete entity if it is approved by user
                            viewRef.get().showDeleteSuccessMessage(entityName, rejectCallback);
                        } else {
                            rejectCallback.set(true); // reject
                            viewRef.get().showDeleteFailMessage(entityName, recipesCount);
                        }
                    }
                }));
    }

    public void editValues(EntityListView view, final BaseEntity entity, String... values) {

        if (entity instanceof Author) {
            storageLogic.updateSync((Author) entity, values[0], false);
            getComposite().add(Completable
                    .fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            storageLogic.updateAsync((Author) entity);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        } else if (entity instanceof Category) {
            storageLogic.updateSync((Category) entity, values[0], false);
            getComposite().add(Completable
                    .fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            storageLogic.updateAsync((Category) entity);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        } else if (entity instanceof Label) {
            storageLogic.updateSync((Label) entity, values[0]);
            getComposite().add(Completable
                    .fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            storageLogic.updateAsync((Label) entity);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        } else if (entity instanceof Food) {
            storageLogic.updateSync((Food) entity, values[0]);
            if (values.length > 1) {
                final String ndbNo = values[1];
                final WeakReference<EntityListView> viewRef = new WeakReference<>(view);
                getComposite().add(Maybe
                        .fromCallable(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return storageLogic.updateAsync((Food) entity, ndbNo);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean cloudUpdate) throws Exception {
                                if (cloudUpdate && viewRef.get() != null)
                                    viewRef.get().showUpdate(getModels().indexOf(entity));
                            }
                        })
                );
            }
        }
    }

    public void editPhoto(EntityListView view, BaseEntity entity) {

        final WeakReference<EntityListView> viewRef = new WeakReference<>(view);

        if (entity instanceof Author) {
            final Author author = (Author) entity;
            final MutableString listener = new MutableString() {
                @Override
                public synchronized MutableString set(final String value) {
                    storageLogic.updateSync(author, value, true);
                    if (viewRef.get() != null)
                        viewRef.get().showUpdate(getModels().indexOf(author));

                    getComposite().add(Completable
                            .fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    storageLogic.updateAsync(author);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .subscribe());
                    return super.set(value);
                }
            };
            editListeners.add(listener);
            view.showPhotoEditDialog(author.getPhoto(), listener);
            return;
        }

        if (entity instanceof Category) {
            final Category category = (Category) entity;
            final MutableString listener = new MutableString() {
                @Override
                public synchronized MutableString set(final String value) {
                    storageLogic.updateSync(category, value, true);
                    if (viewRef.get() != null)
                        viewRef.get().showUpdate(getModels().indexOf(category));

                    getComposite().add(Completable
                            .fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    storageLogic.updateAsync(category);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .subscribe());
                    return super.set(value);
                }
            };
            editListeners.add(listener);
            view.showPhotoEditDialog(category.getPhoto(), listener);
            return;
        }
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

    private MutableBoolean getDeleteRejectCallback(final WeakReference<EntityListView> viewRef,
                                                   final BaseEntity entity, final int position) {
        final MutableBoolean callback = new MutableBoolean() {
            @Override
            public synchronized MutableBoolean set(boolean rejected) {
                if (rejected) {
                    getModels().add(position, entity);
                    if (viewRef.get() != null) viewRef.get().showInsert(position);
                } else {
                    if (entity instanceof Category)
                        businessLogic
                                .getCategoryEventSubject()
                                .onNext(new CategoryEvent(
                                        CategoryAction.DELETE, entity.getUuid()));
                    if (entity instanceof Label)
                        businessLogic
                                .getLabelEventSubject()
                                .onNext(new LabelEvent(
                                        LabelAction.DELETE, entity.getUuid()));
                    getComposite().add(Completable
                            .fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    storageLogic.deleteAsync(entity);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action() {
                                @Override
                                public void run() throws Exception {
                                    Integer position = getPosition(entity.getUuid());
                                    if (position != null) {
                                        getModels().remove((int) position);
                                        viewRef.get().showDelete(position);
                                    }
                                }
                            }));
                }
                return super.set(rejected);
            }
        };
        deleteListeners.add(callback);
        return callback;
    }
}
