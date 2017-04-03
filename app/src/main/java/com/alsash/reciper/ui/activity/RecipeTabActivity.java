package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.adapter.RecipePagerAdapter;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;

/**
 * The Activity that represents two tabs with RecipePagerAdapter:
 * Tab one - RecipeCategoriesFragment
 * Tab two - RecipeSingleListFragment
 */
public class RecipeTabActivity extends BaseTabActivity {

    // Adapters
    private RecipePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getResources().getString(R.string.navigation_recipes));
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.navigation_recipes;
    }

    @Override
    protected SwipePagerAdapter getPagerAdapter() {
        if (pagerAdapter == null) {
            pagerAdapter = new RecipePagerAdapter(this, getSupportFragmentManager());
        }
        return pagerAdapter;
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
}
