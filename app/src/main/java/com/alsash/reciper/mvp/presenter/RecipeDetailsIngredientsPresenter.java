package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.action.RecipeAction;
import com.alsash.reciper.logic.event.RecipeEvent;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.logic.unit.WeightUnit;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.view.RecipeDetailsIngredientsView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Notification;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Simple presenter for its view
 */
public class RecipeDetailsIngredientsPresenter
        extends BaseRestrictPresenter<RecipeDetailsIngredientsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private final List<Ingredient> ingredients;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<MutableBoolean> deleteListeners = new ArrayList<>();

    private RecipeFull recipeFull;

    public RecipeDetailsIngredientsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
        this.ingredients = Collections.synchronizedList(new ArrayList<Ingredient>());
    }

    @Override
    public RecipeDetailsIngredientsPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsIngredientsPresenter) super.setRestriction(restriction);
    }

    @Override
    public void visible(RecipeDetailsIngredientsView view) {
        if (recipeFull == null) return;
        int wgt = (int) Math.round(businessLogic.getRecipeWeight(recipeFull, WeightUnit.GRAM));
        view.showWeight(wgt, WeightUnit.GRAM);
        view.showIngredients(ingredients);
        WeakReference<RecipeDetailsIngredientsView> viewRef = new WeakReference<>(view);
        searchDelete(viewRef);
        searchInsert(viewRef);
    }

    @Override
    public void attach(RecipeDetailsIngredientsView view) {
        super.attach(view);
        final WeakReference<RecipeDetailsIngredientsView> viewRef = new WeakReference<>(view);
        getComposite().add(
                businessLogic.getRecipeEventSubject()
                        .doOnEach(new Consumer<Notification<RecipeEvent>>() {
                            @Override
                            public void accept(@NonNull Notification<RecipeEvent> notification)
                                    throws Exception {
                                RecipeEvent event = notification.getValue();
                                if (event == null) return;
                                switch (event.action) {
                                    case CREATE_INGREDIENT:
                                        searchInsert(viewRef);
                                        break;
                                }
                            }
                        })
                        .subscribe());
    }

    @Override
    public void invisible(RecipeDetailsIngredientsView view) {
        // do nothing
    }

    @Override
    public void detach() {
        super.detach();
        deleteListeners.clear();
    }


    public void requestWeightEdit(RecipeDetailsIngredientsView view, String weight) {
        if (recipeFull == null) return;
        int wgt = 0; // weight
        try {
            wgt = Math.round(Float.parseFloat(weight));
        } catch (Throwable e) {
            view.showWeight(wgt, WeightUnit.GRAM);
            return;
        }
        // Update weight
        if (wgt != (int) Math.round(businessLogic.getRecipeWeight(recipeFull, WeightUnit.GRAM))) {
            Map<String, Double> ingWeight = businessLogic.getIngredientsWeight(recipeFull, wgt);
            storageLogic.updateSync(recipeFull.getIngredients(), ingWeight);
            wgt = (int) Math.round(businessLogic.getRecipeWeight(recipeFull, WeightUnit.GRAM));
            view.showIngredients(ingredients); // same objects as in recipeFull
            view.showWeight(wgt, WeightUnit.GRAM);
            getComposite().add(Completable
                    .fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            storageLogic.updateAsync(recipeFull.getIngredients());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }
    }

    public void requestIngredientEdit(RecipeDetailsIngredientsView view,
                                      Ingredient ingredient, String name, int weight) {
        if (ingredient == null || ingredient.getId() == null) return;
        boolean weightUpdated = weight != (int) Math.round(ingredient.getWeight());
        boolean nameUpdated = (name != null) && !ingredient.getName().equals(name);

        if (weightUpdated || nameUpdated) {
            final Ingredient ing = ingredient;
            storageLogic.updateSync(ingredient, name, weight);
            if (weightUpdated) {
                int wgt = (int) Math.round(businessLogic.getRecipeWeight(recipeFull,
                        WeightUnit.GRAM));
                view.showWeight(wgt, WeightUnit.GRAM);
            }
            getComposite().add(Completable
                    .fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            storageLogic.updateAsync(ing);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe());
        }
    }

    public void requestIngredientAdd(RecipeDetailsIngredientsView view) {
        if (recipeFull != null)
            view.showIngredientsAddDialog(recipeFull);
    }

    public void requestIngredientDelete(RecipeDetailsIngredientsView view, int position) {
        // Instant remove entity
        final Ingredient ing = ingredients.remove(position);
        if (ing == null) return;
        view.showIngredientDelete(position);

        final String entityName = businessLogic.getEntityName(ing);
        final WeakReference<RecipeDetailsIngredientsView> viewRef = new WeakReference<>(view);
        final MutableBoolean rejectCallback = getDeleteRejectCallback(viewRef, ing, position);

        // Delete entity if it is approved by user
        view.showIngredientDeleteMessage(entityName, rejectCallback);
    }

    private void searchDelete(@Nullable WeakReference<RecipeDetailsIngredientsView> viewRef) {
        if (recipeFull == null) return;
        Set<Ingredient> deleted = new HashSet<>();
        for (Ingredient ingExisted : ingredients) {
            boolean foundExisted = false;
            for (Ingredient recipeIngredient : recipeFull.getIngredients()) {
                if (ingExisted.getUuid().equals(recipeIngredient.getUuid())) {
                    foundExisted = true;
                    break;
                }
            }
            if (!foundExisted || ingExisted.getId() == null) deleted.add(ingExisted);
        }
        for (Ingredient ingDeleted : deleted) {
            int position = ingredients.indexOf(ingDeleted);
            if (position >= 0) {
                ingredients.remove(position);
                if (viewRef != null && viewRef.get() != null)
                    viewRef.get().showIngredientDelete(position);
            }
        }
        if (deleted.size() > 0 && viewRef != null && viewRef.get() != null) {
            int wgt = (int) Math.round(businessLogic.getRecipeWeight(recipeFull, WeightUnit.GRAM));
            viewRef.get().showWeight(wgt, WeightUnit.GRAM);
        }
    }

    private void searchInsert(@Nullable WeakReference<RecipeDetailsIngredientsView> viewRef) {
        searchInsert(viewRef, 0);
    }

    private void searchInsert(@Nullable WeakReference<RecipeDetailsIngredientsView> viewRef,
                              int position) {
        if (recipeFull == null) return;
        Set<Ingredient> inserted = new HashSet<>();
        for (Ingredient recipeIngredient : recipeFull.getIngredients()) {
            boolean found = false;
            for (Ingredient ingExisted : ingredients) {
                if (ingExisted.getUuid().equals(recipeIngredient.getUuid())) {
                    found = true;
                    break;
                }
            }
            if (!found && recipeIngredient.getId() != null) inserted.add(recipeIngredient);
        }
        for (Ingredient ingInserted : inserted) {
            ingredients.add(position, ingInserted);
            if (viewRef != null && viewRef.get() != null)
                viewRef.get().showIngredientInsert(position);
        }
        if (inserted.size() > 0 && viewRef != null && viewRef.get() != null) {
            int wgt = (int) Math.round(businessLogic.getRecipeWeight(recipeFull, WeightUnit.GRAM));
            viewRef.get().showWeight(wgt, WeightUnit.GRAM);
        }
    }

    @Nullable
    @Override
    protected BaseEntity getEntity() {
        return recipeFull;
    }

    @Override
    protected void setEntity(@Nullable BaseEntity entity) {
        if (entity == null) {
            recipeFull = null;
            ingredients.clear();
        } else {
            recipeFull = (RecipeFull) entity;
            ingredients.addAll(recipeFull.getIngredients());
        }
    }

    private MutableBoolean getDeleteRejectCallback(
            final WeakReference<RecipeDetailsIngredientsView> viewRef,
            Ingredient ingredient,
            int position) {

        final Integer currentPosition = position;

        final Set<String> ingredientUuid = new HashSet<>();
        ingredientUuid.add(ingredient.getUuid());

        final MutableBoolean callback = new MutableBoolean() {
            @Override
            public synchronized MutableBoolean set(boolean rejected) {
                if (rejected) {
                    searchInsert(viewRef, currentPosition);
                } else {
                    getComposite().add(Completable
                            .fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    storageLogic.updateAsync(recipeFull,
                                            RecipeAction.DELETE_INGREDIENT,
                                            ingredientUuid);
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
        deleteListeners.add(callback);
        return callback;
    }
}
