package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsMethodsView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsMethodsPresenter extends BaseRestrictPresenter<RecipeDetailsMethodsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private final List<Method> methods;
    private final Set<MutableBoolean> callbacks;
    private RecipeFull recipeFull;

    public RecipeDetailsMethodsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
        this.methods = Collections.synchronizedList(new ArrayList<Method>());
        this.callbacks = Collections.synchronizedSet(new HashSet<MutableBoolean>());
    }

    @Override
    public RecipeDetailsMethodsPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsMethodsPresenter) super.setRestriction(restriction);
    }

    @Override
    public void visible(RecipeDetailsMethodsView view) {
        if (recipeFull == null) return;
        view.showMethods(methods);
        WeakReference<RecipeDetailsMethodsView> viewRef = new WeakReference<>(view);
        searchDelete(viewRef);
        searchInsert(viewRef, 0);
    }

    @Override
    public void invisible(RecipeDetailsMethodsView view) {
        // Do nothing
    }

    public void onMethodEdit(final Method method, String body) {
        storageLogic.updateSync(method, body);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateAsync(method);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void onMethodsMove(RecipeDetailsMethodsView view, int fromPosition, int toPosition) {
        Collections.swap(methods, fromPosition, toPosition);
        for (int i = 0; i < methods.size(); i++) {
            storageLogic.updateSync(methods.get(i), i);
        }
        view.showMethodMove(fromPosition, toPosition);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        for (Method method : methods) storageLogic.updateAsync(method);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void onMethodAdd(RecipeDetailsMethodsView view) {
        final WeakReference<RecipeDetailsMethodsView> viewRef = new WeakReference<>(view);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storageLogic.updateAsync(recipeFull,
                                RecipeAction.CREATE_METHOD,
                                new HashSet<String>());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        searchInsert(viewRef, 0);
                    }
                }));
    }

    public void onMethodDelete(RecipeDetailsMethodsView view, int position) {
        final Method method = methods.remove(position);
        if (method == null) return;
        view.showMethodDelete(position);
        // Approve or reject callback
        final WeakReference<RecipeDetailsMethodsView> viewRef = new WeakReference<>(view);
        final Set<String> deletedUuid = new HashSet<>();
        deletedUuid.add(method.getUuid());
        final Integer pos = position;
        final MutableBoolean reject = new MutableBoolean() {
            @Override
            public synchronized MutableBoolean set(boolean rejected) {
                if (rejected) {
                    searchInsert(viewRef, pos);
                } else {
                    getComposite().add(Completable
                            .fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    storageLogic.updateAsync(recipeFull,
                                            RecipeAction.DELETE_METHOD,
                                            deletedUuid);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action() {
                                @Override
                                public void run() throws Exception {
                                    searchDelete(viewRef);
                                }
                            }));
                }
                return super.set(rejected);
            }
        };
        view.showMethodDeleteMessage(position, reject);
    }

    private void searchDelete(@Nullable WeakReference<RecipeDetailsMethodsView> viewRef) {
        if (recipeFull == null) return;
        Set<Method> deleted = new HashSet<>();
        for (Method mExisted : methods) {
            boolean found = false;
            for (Method method : recipeFull.getMethods()) {
                if (mExisted.getUuid().equals(method.getUuid())) {
                    found = true;
                    break;
                }
            }
            if (!found || mExisted.getId() == null) deleted.add(mExisted);
        }
        for (Method mDeleted : deleted) {
            int position = methods.indexOf(mDeleted);
            if (position >= 0) {
                methods.remove(position);
                if (viewRef != null && viewRef.get() != null)
                    viewRef.get().showMethodDelete(position);
            }
        }
    }

    private void searchInsert(@Nullable WeakReference<RecipeDetailsMethodsView> viewRef,
                              int position) {
        if (recipeFull == null) return;
        Set<Method> inserted = new HashSet<>();
        for (Method method : recipeFull.getMethods()) {
            boolean found = false;
            for (Method mExisted : methods) {
                if (mExisted.getUuid().equals(method.getUuid())) {
                    found = true;
                    break;
                }
            }
            if (!found && method.getId() != null) inserted.add(method);
        }
        for (Method ingInserted : inserted) {
            methods.add(position, ingInserted);
            if (viewRef != null && viewRef.get() != null)
                viewRef.get().showMethodInsert(position);
        }
    }

    @Nullable
    @Override
    protected BaseEntity getEntity() {
        return recipeFull;
    }

    @Override
    protected void setEntity(@Nullable BaseEntity entity) {
        recipeFull = (RecipeFull) entity;
        if (recipeFull == null) {
            methods.clear();
        } else {
            methods.addAll(recipeFull.getMethods());
        }
    }
}
