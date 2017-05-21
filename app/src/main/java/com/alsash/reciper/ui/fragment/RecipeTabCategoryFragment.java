package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupedPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabCategoryPresenter;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;
import com.alsash.reciper.ui.adapter.RecipeCategoryCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A final fragment with presenter, that represents group of recipes and their interactions
 */
public class RecipeTabCategoryFragment extends BaseRecipeListFragment<Category, RecipeTabCategoryView>
        implements RecipeTabCategoryView, RecipeGroupInteraction {

    @Inject
    RecipeTabCategoryPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeTabCategoryFragment newInstance() {
        return new RecipeTabCategoryFragment();
    }

    @Override
    protected RecipeTabCategoryPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getRecipeTabComponent()
                .inject(this);
        return presenter; // Presenter will be embedded in fragment lifecycle
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Category> categories) {
        return new RecipeCategoryCardListAdapter(categories, this, this);
    }

    @Override
    public BaseRecipeGroupedPresenter getInnerPresenter(long groupId) {
        return presenter.getInnerPresenter(groupId);
    }

    @Override
    protected AppNavigator getNavigator() {
        return navigator;
    }
}
