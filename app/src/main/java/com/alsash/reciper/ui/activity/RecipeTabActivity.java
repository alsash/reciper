package com.alsash.reciper.ui.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;
import com.alsash.reciper.ui.application.ReciperApplication;

import javax.inject.Inject;

public class RecipeTabActivity extends BaseSwipeTabActivity {

    @Inject
    RecipeTabPresenter presenter;

    @Override
    protected BasePresenter setupPresenter() {
        ((ReciperApplication) getApplicationContext())
                .getAppComponent()
                .getRecipeTabComponentBuilder()
                .build()
                .inject(this);
        presenter.setView(this); // Parent Activity implements SwipeTabView
        return presenter; // Presenter will be embedded in activity lifecycle
    }

    @Override
    protected void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.navigation_recipes;
    }
}
