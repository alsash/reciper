package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.view.BaseSelectionDialogView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A BaseEntity selection dialog
 */
public abstract class BaseSelectionDialogPresenter<M extends BaseEntity,
        V extends BaseSelectionDialogView<M>>
        extends BaseListPresenter<M, V> {

    private final StorageLogic storageLogic;
    private final Set<String> selectionUuid;
    private final boolean multiSelection;

    private EntityRestriction restriction;
    private BaseEntity restrictedEntity;

    public BaseSelectionDialogPresenter(int limit,
                                        boolean multiSelection,
                                        StorageLogic storageLogic) {
        super(limit);
        this.multiSelection = multiSelection;
        this.storageLogic = storageLogic;
        this.selectionUuid = Collections.synchronizedSet(new LinkedHashSet<String>());
    }

    public BaseSelectionDialogPresenter<M, V> setRestriction(EntityRestriction restriction) {
        this.restriction = restriction;
        setRestrictedEntity(null);
        selectionUuid.clear();
        detach();
        return this;
    }

    protected abstract Set<String> getRestrictedUuid(BaseEntity entity);

    protected abstract boolean updateDbOnApproved();

    protected abstract void updateUiOnApproved(boolean dbUpdated);

    @Override
    public void attach(V view) {
        super.attach(view);
        if (getRestrictedEntity() != null) return;
        final WeakReference<V> viewRef = new WeakReference<>(view);
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
                        setRestrictedEntity(loadedEntity);
                        selectionUuid.addAll(getRestrictedUuid(loadedEntity));
                        if (viewRef.get() != null)
                            viewRef.get().setSelection(multiSelection, selectionUuid);
                    }
                })
        );
    }

    public void select(String uuid) {
        // do nothing. Adapter must update selectionUuid.
    }

    public void approve(V view) {
        if (!approved()) {
            view.finishView();
            return;
        }
        final WeakReference<V> viewRef = new WeakReference<>(view);
        getComposite().add(Maybe
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return updateDbOnApproved();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        updateUiOnApproved(aBoolean);
                        if (viewRef.get() != null) viewRef.get().finishView();
                    }
                })
        );
    }

    protected boolean approved() {
        boolean same = selectionUuid.equals(getRestrictedUuid(getRestrictedEntity()));
        if (multiSelection) {
            if (same) return false;
        } else {
            if (selectionUuid.size() == 0 || same) return false;
        }
        return true;
    }

    @Nullable
    protected BaseEntity getRestrictedEntity() {
        return restrictedEntity;
    }

    protected void setRestrictedEntity(@Nullable BaseEntity entity) {
        restrictedEntity = entity;
    }

    protected Set<String> getSelectionUuid() {
        return selectionUuid;
    }
}
