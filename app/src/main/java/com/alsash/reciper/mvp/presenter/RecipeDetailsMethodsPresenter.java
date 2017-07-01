package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsMethodsView;

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
    }

    @Override
    public void invisible(RecipeDetailsMethodsView view) {
        // Update methods indexes
        if (methods == null) return;
        for (int i = 0; i < methods.size(); i++) {
            storageLogic.updateSync(methods.get(i), i);
        }
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
