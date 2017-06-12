package com.alsash.reciper.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeGroupInnerPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionLabelPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionLabelView;
import com.alsash.reciper.ui.adapter.RecipeLabelCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment with presenter, that represents group of recipes and their interactions
 */
public class RecipeCollectionLabelFragment
        extends BaseListFragment<Label, RecipeCollectionLabelView>
        implements RecipeCollectionLabelView, RecipeGroupInteraction<Label>, RecipeListInteraction {

    @Inject
    RecipeCollectionLabelPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeCollectionLabelFragment newInstance() {
        return new RecipeCollectionLabelFragment();
    }

    @Override
    public void onPin(Recipe recipe) {

    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe.getId());
    }

    @Override
    public BaseRecipeGroupInnerPresenter<Label> injectInnerPresenter(Label label) {
        return presenter.getInnerPresenter(label);
    }

    @Override
    protected RecipeCollectionLabelPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiRecipeCollectionsComponent()
                .inject(this);
        return presenter; // Presenter will be embedded in fragment lifecycle
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Label> labels) {
        return new RecipeLabelCardListAdapter(labels, this, this);
    }
}
