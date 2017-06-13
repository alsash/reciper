package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeCollectionGridPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionGridView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent list of recipe cards
 */
public class RecipeCollectionGridFragment
        extends BaseListFragment<Recipe, RecipeCollectionGridView>
        implements RecipeCollectionGridView, RecipeListInteraction {

    @Inject
    RecipeCollectionGridPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeCollectionGridFragment newInstance() {
        return new RecipeCollectionGridFragment();
    }

    @Override
    public void onFavorite(Recipe recipe) {
        // Do nothing
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe.getId());
    }

    @Override
    protected RecipeCollectionGridPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiRecipeCollectionsComponent()
                .inject(this);
        return presenter; // Presenter will be embedded in fragment lifecycle
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Recipe> container) {
        return new RecipeCardListAdapter(this, container, R.layout.item_recipe_card);
    }

    @Override
    protected void setupList() {
        super.setupList();
        list.setItemAnimator(new FlipCardListAnimator());
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context,
                context.getResources().getInteger(R.integer.recipe_list_span));
    }
}
