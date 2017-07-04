package com.alsash.reciper.mvp.presenter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alsash.reciper.R;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.view.RecipeDetailsView;
import com.alsash.reciper.ui.fragment.RecipeDetailsDescriptionFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsIngredientsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsMethodsFragment;
import com.alsash.reciper.ui.fragment.RecipeDetailsNutritionFragment;

/**
 * A Presenter that represents details of a single recipe
 */
public class RecipeDetailsPresenter extends BaseRestrictPresenter<RecipeDetailsView> {

    private final StorageLogic storageLogic;
    private final BusinessLogic businessLogic;

    private RecipeFull recipeFull;
    private SwipeTab[] swipeTabs;
    private int shownPosition;

    public RecipeDetailsPresenter(StorageLogic storageLogic, BusinessLogic businessLogic) {
        super(storageLogic);
        this.storageLogic = storageLogic;
        this.businessLogic = businessLogic;
    }

    @Override
    public RecipeDetailsPresenter setRestriction(EntityRestriction restriction) {
        return (RecipeDetailsPresenter) super.setRestriction(restriction);
    }

    public RecipeDetailsPresenter setFragments(Fragment[] fragments) {
        swipeTabs = new SwipeTab[fragments.length];
        for (int i = 0; i < fragments.length; i++) {

            if (fragments[i] instanceof RecipeDetailsDescriptionFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_details_description_title)
                        .swiped(true)
                        .build();
            } else if (fragments[i] instanceof RecipeDetailsNutritionFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_details_nutrition_title)
                        .swiped(true)
                        .build();
            } else if (fragments[i] instanceof RecipeDetailsIngredientsFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_details_ingredients_title)
                        .swiped(true)
                        .build();
            } else if (fragments[i] instanceof RecipeDetailsMethodsFragment) {
                swipeTabs[i] = SwipeTab.builder()
                        .fragment(fragments[i])
                        .title(R.string.fragment_recipe_details_methods_title)
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
        super.attach(view);
    }

    @Override
    public void visible(RecipeDetailsView view) {
        view.showDetail(shownPosition);
        if (recipeFull == null) return;
        view.showTitle(recipeFull.getName());
        view.showPhoto(recipeFull.getMainPhoto());
    }

    @Override
    public void invisible(RecipeDetailsView view) {
        shownPosition = view.shownDetail();
    }

    @Nullable
    @Override
    protected BaseEntity getEntity() {
        return recipeFull;
    }

    @Override
    protected void setEntity(@Nullable BaseEntity entity) {
        recipeFull = (RecipeFull) entity;
    }
}
