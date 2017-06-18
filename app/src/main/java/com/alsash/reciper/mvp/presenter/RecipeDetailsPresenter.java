package com.alsash.reciper.mvp.presenter;

import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.model.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.RecipeDetailsView;
import com.alsash.reciper.ui.fragment.RecipeDetailDescriptionsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailIngredientsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailMethodsFragment;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * A Presenter that represents details of a single recipe
 */
public class RecipeDetailsPresenter implements BasePresenter<RecipeDetailsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;
    private EntityRestriction restriction;
    private RecipeFull recipeFull;
    private SwipeTab[] swipeTabs;
    private CompositeDisposable composite = new CompositeDisposable();
    private boolean recipeShown;

    public RecipeDetailsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    public RecipeDetailsPresenter setRestriction(EntityRestriction restriction) {
        this.restriction = restriction;
        this.recipeFull = null;
        detach();
        return this;
    }

    public RecipeDetailsPresenter setFragments(Fragment[] fragments) {
        swipeTabs = new SwipeTab[fragments.length];
        for (int i = 0; i < fragments.length; i++) {

            if (fragments[i] instanceof RecipeDetailDescriptionsFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_detail_descriptions_title)
                        // .icon(R.drawable.activity_recipe_details_tab_description)
                        .swiped(true)
                        .build();
            } else if (fragments[i] instanceof RecipeDetailIngredientsFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_detail_ingredients_title)
                        // .icon(R.drawable.activity_recipe_details_tab_ingredients)
                        .swiped(true)
                        .build();
            } else if (fragments[i] instanceof RecipeDetailMethodsFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_detail_methods_title)
                        // .icon(R.drawable.activity_recipe_details_tab_methods)
                        .swiped(true)
                        .build();
            } else {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .swiped(true)
                        .build();
            }
        }
        return this;
    }

    @Override
    public void attach(final RecipeDetailsView view) {
        view.setDetails(swipeTabs);
        if (recipeFull != null) return;
        final WeakReference<RecipeDetailsView> viewRef = new WeakReference<>(view);
        composite.add(Maybe
                .fromCallable(new Callable<RecipeFull>() {
                    @Override
                    public RecipeFull call() throws Exception {
                        return (RecipeFull) storageLogic.getRestrictionEntity(restriction);
                    }
                }).subscribe(new Consumer<RecipeFull>() {
                    @Override
                    public void accept(@NonNull RecipeFull loadedRecipe) throws Exception {
                        recipeFull = loadedRecipe;
                        if (viewRef.get() != null && viewRef.get().isViewVisible())
                            visible(viewRef.get());
                    }
                })
        );
    }

    @Override
    public void visible(RecipeDetailsView view) {
        if (recipeFull == null) return;
        if (recipeShown) return;
        view.showTitle(recipeFull.getName());
        view.showPhoto(recipeFull.getMainPhoto());
        recipeShown = true;
    }

    @Override
    public void invisible(RecipeDetailsView view) {

    }

    @Override
    public void refresh(RecipeDetailsView view) {
        detach();
        attach(view);
    }

    @Override
    public void detach() {
        composite.dispose();
        composite.clear();
        composite = new CompositeDisposable();
        recipeShown = false;
    }
}
