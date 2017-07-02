package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.view.BaseView;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * A Presenter that represents details of a single recipe
 */
public abstract class BaseRestrictPresenter<V extends BaseView> implements BasePresenter<V> {

    private final StorageLogic storageLogic;
    private EntityRestriction restriction;
    private CompositeDisposable composite = new CompositeDisposable();

    public BaseRestrictPresenter(StorageLogic storageLogic) {
        this.storageLogic = storageLogic;
    }

    public BaseRestrictPresenter<V> setRestriction(EntityRestriction restriction) {
        if (!isNewRestriction(restriction)) return this;
        this.restriction = restriction;
        setEntity(null);
        detach();
        return this;
    }

    @Override
    public void attach(final V view) {
        if (getEntity() != null) return;
        final WeakReference<V> viewRef = new WeakReference<>(view);
        composite.add(Maybe
                .fromCallable(new Callable<BaseEntity>() {
                    @Override
                    public BaseEntity call() throws Exception {
                        return storageLogic.getRestrictEntity(restriction);
                    }
                }).subscribe(new Consumer<BaseEntity>() {
                    @Override
                    public void accept(@NonNull BaseEntity loadedEntity) throws Exception {
                        setEntity(loadedEntity);
                        if (viewRef.get() != null && viewRef.get().isViewVisible())
                            visible(viewRef.get());
                    }
                })
        );
    }

    @Override
    public void refresh(V view) {
        detach();
        attach(view);
    }

    @Override
    public void detach() {
        composite.dispose();
        composite.clear();
        composite = new CompositeDisposable();
    }

    @Nullable
    protected abstract BaseEntity getEntity();

    protected abstract void setEntity(@Nullable BaseEntity entity);

    protected boolean isNewRestriction(EntityRestriction restriction) {
        return this.restriction == null || !this.restriction.equals(restriction);
    }

    protected CompositeDisposable getComposite() {
        return composite;
    }
}
