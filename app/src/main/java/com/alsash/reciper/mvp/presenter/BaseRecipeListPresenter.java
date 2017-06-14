package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeListView;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * A Recipe List Presenter, that implement Recipe actions logic
 */
public abstract class BaseRecipeListPresenter<V extends RecipeListView>
        extends BaseListPresenter<Recipe, V> {

    private final StorageLogic storage;

    public BaseRecipeListPresenter(int limit, StorageLogic storage) {
        super(limit);
        this.storage = storage;
    }

    public void changeFavorite(final Recipe recipe) {
        storage.updateSync(recipe, RecipeAction.FAVORITE);
        getComposite().add(Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        storage.updateAsync(recipe);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }
}
