package com.alsash.reciper.ui.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.restriction.EntityRestriction;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.alsash.reciper.mvp.presenter.RecipeAuthorDialogPresenter;
import com.alsash.reciper.mvp.view.RecipeAuthorDialogView;
import com.alsash.reciper.ui.adapter.EntitySelectionAdapter;
import com.alsash.reciper.ui.adapter.interaction.EntitySelectionInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * Complex Category selection dialog
 */
public class RecipeAuthorDialogFragment
        extends BaseDialogListFragment<Author, RecipeAuthorDialogView>
        implements RecipeAuthorDialogView, EntitySelectionInteraction {

    @Inject
    RecipeAuthorDialogPresenter presenter;

    public static void start(FragmentManager fragmentManager, Recipe recipe) {
        RecipeAuthorDialogFragment fragment = new RecipeAuthorDialogFragment();
        Bundle args = new Bundle();
        args.putString(AppContract.Key.RECIPE_ID, recipe.getUuid());
        fragment.setArguments(args);
        fragment.show(fragmentManager, fragment.getTag());
    }

    @Override
    public void onSelect(BaseEntity entity) {
        presenter.onSelect((Author) entity);
    }

    @Override
    public void setSelected(Author author) {
        if (adapter != null)
            ((EntitySelectionAdapter) adapter).setSelectedEntity(author.getUuid());
    }

    @Override
    public void finishView() {
        dismiss();
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Author> container) {
        return new EntitySelectionAdapter(this, container, Author.class);
    }

    @Nullable
    @Override
    protected String getTitle() {
        return getString(R.string.entity_category);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_author_dialog, container, false);
        list = (RecyclerView) layout.findViewById(R.id.recipe_author_list);

        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected RecipeAuthorDialogPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiDialogComponent()
                .inject(this);
        return presenter.setRestriction(new EntityRestriction(
                getArguments().getString(AppContract.Key.RECIPE_ID),
                RecipeFull.class));
    }

    protected void clickPositive() {
        presenter.onApprove(getThisView());
    }

    protected void clickNegative() {
        dismiss();
    }

}
