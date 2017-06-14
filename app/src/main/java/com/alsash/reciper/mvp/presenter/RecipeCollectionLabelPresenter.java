package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeCollectionLabelView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Notification;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A Presenter that represents collection of Recipes grouped by Labels
 */
public class RecipeCollectionLabelPresenter
        extends BaseRecipeGroupPresenter<Label, RecipeCollectionLabelView> {

    private static final int PAGINATION_LABEL_LIMIT = 10;
    private static final int PAGINATION_RECIPE_LIMIT = 10;

    private final StorageLogic storage;

    public RecipeCollectionLabelPresenter(StorageLogic storage) {
        super(PAGINATION_LABEL_LIMIT, PAGINATION_RECIPE_LIMIT);
        this.storage = storage;
    }

    @Override
    protected StorageLogic getStorageLogic() {
        return storage;
    }

    @Override
    protected List<Label> loadNextGroups(int offset, int limit) {
        return storage.getLabels(offset, limit);
    }

    @Override
    public List<Recipe> loadNextRecipes(Label label, int offset, int limit) {
        return storage.getRecipes(label, offset, limit);
    }

    public void changeFavorite(final Recipe recipe) {
        storage.updateSync(recipe, RecipeAction.FAVORITE);
        Flowable
                .fromCallable(new Callable<List<Label>>() {
                    @Override
                    public List<Label> call() throws Exception {
                        storage.updateAsync(recipe);
                        return storage.getLabels(recipe);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(new Consumer<Notification<List<Label>>>() {
                    @Override
                    public void accept(@NonNull Notification<List<Label>> listNotification) throws Exception {
                        List<Label> labels = listNotification.getValue();
                        if (labels == null) return;
                        for (Map.Entry<Label, RecipeGroupInnerPresenter<Label>> presenterEntry :
                                getPresenters().entrySet()) {
                            for (Label recipeLabel : labels) {
                                Label label = presenterEntry.getKey();
                                if (label.equals(recipeLabel) ||
                                        label.getId().equals(recipeLabel.getId())) {
                                    presenterEntry.getValue().updateView(recipe, recipeLabel);
                                }
                            }
                        }
                    }
                })
                .subscribe();
    }
}
