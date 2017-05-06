package com.alsash.reciper.ui.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeTabPresenter;
import com.alsash.reciper.mvp.view.RecipeTabView;

import javax.inject.Inject;

public class RecipeTabActivity extends BaseSwipeTabActivity<RecipeTabView>
        implements RecipeTabView {

    @Inject
    RecipeTabPresenter presenter; // Parent BaseSwipeTabActivity implements SwipeTabView

    @Override
    protected BasePresenter<RecipeTabView> inject() {
        ((ReciperApp) getApplicationContext())
                .getRecipeTabComponent()
                .inject(this);
        return presenter; // BasePresenter will be embedded in the activity lifecycle
    }

    @Override
    protected void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFabClick(getThisView());
            }
        });
    }

    @Override
    public void showNotification(String notification) {
        Snackbar.make(coordinator, notification, Snackbar.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.navigation_recipes;
    }
}
