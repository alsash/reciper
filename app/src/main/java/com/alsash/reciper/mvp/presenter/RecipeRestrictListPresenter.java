package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.restriction.EntityRestriction;
import com.alsash.reciper.mvp.view.RecipeRestrictListView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A Presenter that represents collection of all Recipes
 */
public class RecipeRestrictListPresenter extends BaseRecipeListPresenter<RecipeRestrictListView> {

    private static final int PAGINATION_LIMIT = 20;

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private EntityRestriction restriction;
    private BaseEntity entity;

    public RecipeRestrictListPresenter(StorageLogic storageLogic,
                                       BusinessLogic businessLogic) {
        super(PAGINATION_LIMIT, storageLogic, businessLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    public RecipeRestrictListPresenter setRestriction(EntityRestriction restriction) {
        this.restriction = restriction;
        this.entity = null;
        refresh();
        return this;
    }

    @Override
    public void attach(RecipeRestrictListView view) {
        super.attach(view);
        if (entity == null) {
            final WeakReference<RecipeRestrictListView> viewRef = new WeakReference<>(view);
            getComposite().add(Maybe
                    .fromCallable(new Callable<BaseEntity>() {
                        @Override
                        public BaseEntity call() throws Exception {
                            return storageLogic.getRestrictionEntity(restriction);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<BaseEntity>() {
                        @Override
                        public void accept(@NonNull BaseEntity loadedEntity) throws Exception {
                            entity = loadedEntity;
                            setTitleAndLogo(viewRef.get());
                        }
                    })
            );
        } else {
            setTitleAndLogo(view);
        }
    }

    private void setTitleAndLogo(RecipeRestrictListView view) {
        if (view == null || entity == null) return;
        if (entity instanceof Category) {
            view.setTitle(((Category) entity).getName());
        } else if (entity instanceof Label) {
            view.setTitle(((Label) entity).getName());
        } else {
            view.setTitle(entity.toString());
        }
    }

    @Override
    protected List<Recipe> loadNext(int offset, int limit) {
        return storageLogic.getRestrictRecipes(restriction, offset, limit);
    }
}
