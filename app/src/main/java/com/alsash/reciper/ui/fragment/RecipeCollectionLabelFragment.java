package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeListPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionLabelPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionLabelView;
import com.alsash.reciper.mvp.view.RecipeListView;
import com.alsash.reciper.ui.adapter.RecipeLabelCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment with presenter, that represents group of recipes and their interactions
 */
public class RecipeCollectionLabelFragment
        extends BaseListFragment<Label, RecipeCollectionLabelView>
        implements RecipeCollectionLabelView, RecipeGroupInteraction<Label> {

    @Inject
    RecipeCollectionLabelPresenter presenter;
    @Inject
    AppNavigator navigator;

    public static RecipeCollectionLabelFragment newInstance() {
        return new RecipeCollectionLabelFragment();
    }

    @Override
    public BaseRecipeListPresenter<RecipeListView> injectInnerPresenter(Label label) {
        return presenter.getInnerPresenter(label);
    }

    @Override
    public void onOpen(Label label) {
        navigator.fromActivity(getActivity()).toRecipeListView(label);
    }

    @Override
    public void onOpen(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe);
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
        return new RecipeLabelCardListAdapter(labels, this);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
