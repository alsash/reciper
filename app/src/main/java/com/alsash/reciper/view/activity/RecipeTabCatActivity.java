package com.alsash.reciper.view.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.view.adapter.RecipeCatPagerAdapter;
import com.alsash.reciper.view.adapter.SwipePagerAdapter;

/**
 * The Activity that represents two tabs with RecipeCatPagerAdapter:
 * Tab one - RecipeCategoriesFragment
 * Tab two - RecipeListFragment
 */
public class RecipeTabCatActivity extends BaseTabActivity {

    // Adapters
    private RecipeCatPagerAdapter pagerAdapter;

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.drawer_base_nav_recipe_all;
    }

    @Override
    protected SwipePagerAdapter getPagerAdapter() {
        if (pagerAdapter == null) {
            pagerAdapter = new RecipeCatPagerAdapter(this, getSupportFragmentManager());
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
