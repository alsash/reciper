package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupListPresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabLabelPresenter;
import com.alsash.reciper.mvp.view.RecipeTabLabelView;
import com.alsash.reciper.ui.adapter.RecipeLabelCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;

import java.util.List;

import javax.inject.Inject;

/**
 * A final fragment with presenter, that represents group of recipes and their interactions
 */
public class RecipeTabLabelFragment extends BaseListFragment<Label, RecipeTabLabelView>
        implements RecipeTabLabelView, RecipeGroupInteraction, RecipeListInteraction {

    @Inject
    RecipeTabLabelPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeTabLabelFragment newInstance() {
        return new RecipeTabLabelFragment();
    }

    @Override
    protected RecipeTabLabelPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getRecipeTabComponent()
                .inject(this);
        return presenter; // Presenter will be embedded in fragment lifecycle
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Label> labels) {
        return new RecipeLabelCardListAdapter(labels, this, this);
    }

    @Override
    public BaseRecipeGroupListPresenter getInnerPresenter() {
        return presenter.getInnerPresenter();
    }

    @Override
    public void onExpand(Recipe recipe) {
        RecipeBottomDialog.show(recipe.getId(), getActivity().getSupportFragmentManager());
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.toRecipeView(recipe.getId());
    }
}
