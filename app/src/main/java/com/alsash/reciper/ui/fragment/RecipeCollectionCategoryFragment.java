package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeListPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionCategoryPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionCategoryView;
import com.alsash.reciper.mvp.view.RecipeListView;
import com.alsash.reciper.ui.adapter.RecipeCategoryCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A final fragment with presenter, that represents group of recipes and their interactions
 */
public class RecipeCollectionCategoryFragment
        extends BaseListFragment<Category, RecipeCollectionCategoryView>
        implements RecipeCollectionCategoryView, RecipeGroupInteraction<Category> {

    @Inject
    RecipeCollectionCategoryPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeCollectionCategoryFragment newInstance() {
        return new RecipeCollectionCategoryFragment();
    }

    @Override
    public BaseRecipeListPresenter<RecipeListView> injectInnerPresenter(Category category) {
        return presenter.getInnerPresenter(category);
    }

    @Override
    public void onOpen(Category category) {
        navigator.fromActivity(getActivity()).toRecipeListView(category);
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe);
    }

    @Override
    protected RecipeCollectionCategoryPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiRecipeCollectionsComponent()
                .inject(this);
        return presenter; // Presenter will be embedded in fragment lifecycle
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Category> categories) {
        return new RecipeCategoryCardListAdapter(categories, this);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
