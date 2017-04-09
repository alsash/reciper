package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.R;

/**
 * The Activity that represents two tabs with SwipePagerAdapter:
 * Tab one - RecipeLabelsFragment
 * Tab two - RecipeBookmarksFragment
 */
public class RecipeTabFavActivity extends BaseSwipeTabActivity {

    // Adapters
    private RecipeFavPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getResources().getString(R.string.drawer_base_nav_recipe_favorite));
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
//        return R.id.drawer_base_nav_recipe_favorite;
        return null;
    }

    @Override
    protected SwipePagerAdapter getPagerAdapter() {
        if (pagerAdapter == null) {
            pagerAdapter = new RecipeFavPagerAdapter(this, getSupportFragmentManager());
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
