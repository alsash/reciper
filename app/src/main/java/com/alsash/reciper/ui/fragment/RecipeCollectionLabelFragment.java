package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BaseRecipeListPresenter;
import com.alsash.reciper.mvp.presenter.RecipeCollectionLabelPresenter;
import com.alsash.reciper.mvp.view.RecipeCollectionLabelView;
import com.alsash.reciper.mvp.view.RecipeListView;
import com.alsash.reciper.ui.adapter.RecipeLabelCardListAdapter;
import com.alsash.reciper.ui.adapter.interaction.RecipeGroupInteraction;
import com.alsash.reciper.ui.fragment.dialog.RecipeCreationDialogFragment;

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
    NavigationLogic navigator;

    public static RecipeCollectionLabelFragment newInstance() {
        return new RecipeCollectionLabelFragment();
    }

    @Override
    public BaseRecipeListPresenter<RecipeListView> injectInnerPresenter(Label label) {
        return presenter.getInnerPresenter(label);
    }

    @Override
    public void onOpen(Label label) {
        if (presenter.canOpenGroup(label))
            navigator.fromActivity(getActivity()).toRecipeListView(label);
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
