package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeCollectionGridPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionGridView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;
import com.alsash.reciper.ui.fragment.dialog.RecipeCreationDialogFragment;

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
    NavigationLogic navigator;

    public static RecipeCollectionGridFragment newInstance() {
        return new RecipeCollectionGridFragment();
    }

    @Override
    public void onFavorite(Recipe recipe) {
        presenter.changeFavorite(recipe);
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.appbar_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.appbar_add) {
            RecipeCreationDialogFragment.start(getActivity().getSupportFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
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
