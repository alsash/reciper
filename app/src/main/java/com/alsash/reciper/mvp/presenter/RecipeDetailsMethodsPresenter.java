package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsMethodsView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsMethodsPresenter extends BaseRestrictPresenter<RecipeDetailsMethodsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private RecipeFull recipeFull;
    private List<Method> methods;

    public RecipeDetailsMethodsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public RecipeDetailsMethodsPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsMethodsPresenter) super.setRestriction(restriction);
    }

    @Override
    public void visible(RecipeDetailsMethodsView view) {
        if (methods == null) return;
        view.showMethods(methods);
        view.showMethodsTitle(methods.size());
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

    public void onMethodDelete(RecipeDetailsMethodsView view, final int position) {
        final Method method = methods.remove(position);
        if (method == null) return;
        // Approve or reject callback
        final WeakReference<RecipeDetailsMethodsView> viewRef = new WeakReference<>(view);
        final MutableBoolean reject = new MutableBoolean() {
            @Override
            public synchronized MutableBoolean set(boolean rejected) {
                if (rejected) {
                    methods.add(position, method);
                    if (viewRef.get() != null) {
                        viewRef.get().showMethodsTitle(methods.size());
                        viewRef.get().showMethodAdd(position);
                    }
                } else {
                    getComposite().add(Completable
                            .fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    storageLogic.deleteAsync(method);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .subscribe());
                }
                return super.set(rejected);
            }
        };
        view.showMethodsTitle(methods.size());
        view.showMethodDelete(position, reject);
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
            methods = null;
        } else {
            methods = recipeFull.getMethods();
        }
    }
}
