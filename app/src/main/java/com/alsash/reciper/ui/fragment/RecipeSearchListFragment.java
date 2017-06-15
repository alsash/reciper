package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeSearchListPresenter;
import com.alsash.reciper.mvp.view.RecipeListView;
import com.alsash.reciper.ui.adapter.RecipeCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.animator.FlipCardListAnimator;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent list of recipe cards
 */
public class RecipeSearchListFragment extends BaseListFragment<Recipe, RecipeListView>
        implements RecipeListView, RecipeListInteraction {

    private static final String KEY_CATEGORY_UUID = "CATEGORY_UUID";
    private static final String KEY_LABEL_UUID = "LABEL_UUID";

    @Inject
    RecipeSearchListPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeSearchListFragment newInstance(Category category) {
        if (category == null) return newInstance();
        Bundle args = new Bundle();
        RecipeSearchListFragment fragment = new RecipeSearchListFragment();
        args.putString(KEY_CATEGORY_UUID, category.getUuid());
        fragment.setArguments(args);
        return fragment;
    }

    public static RecipeSearchListFragment newInstance(Label label) {
        if (label == null) return newInstance();
        Bundle args = new Bundle();
        RecipeSearchListFragment fragment = new RecipeSearchListFragment();
        args.putString(KEY_LABEL_UUID, label.getUuid());
        fragment.setArguments(args);
        return fragment;
    }

    public static RecipeSearchListFragment newInstance() {
        return new RecipeSearchListFragment();
    }

    @Override
    public void onFavorite(Recipe recipe) {
        presenter.changeFavorite(recipe);
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe.getId());
    }

    @Override
    protected RecipeSearchListPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiRecipeSearchComponent()
                .inject(this);
        if (getArguments() != null) {
            String categoryUuid = getArguments().getString(KEY_CATEGORY_UUID);
            if (categoryUuid != null)
                return presenter.restrictByCategory(categoryUuid);

            String labelUuid = getArguments().getString(KEY_LABEL_UUID);
            if (labelUuid != null)
                return presenter.restrictByLabel(labelUuid);
        }
        return presenter.noRestriction(); // Presenter will be embedded in fragment lifecycle
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
