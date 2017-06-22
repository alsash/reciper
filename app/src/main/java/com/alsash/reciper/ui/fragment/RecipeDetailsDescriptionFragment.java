package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.presenter.RecipeDetailsDescriptionPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsDescriptionView;

import javax.inject.Inject;

/**
 * A fragment that represent recipe descriptions
 */
public class RecipeDetailsDescriptionFragment extends BaseFragment<RecipeDetailsDescriptionView>
        implements RecipeDetailsDescriptionView {

    @Inject
    RecipeDetailsDescriptionPresenter presenter;
    @Inject
    AppNavigator navigator;


    public static RecipeDetailsDescriptionFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsDescriptionFragment(), intent);
    }

    @Override
    protected RecipeDetailsDescriptionPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(
                R.layout.fragment_recipe_details_description, container, false);
        return layout;
    }
}
