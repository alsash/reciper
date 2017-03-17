package com.alsash.reciper.view.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.adapter.RecipeFavPagerAdapter;
import com.alsash.reciper.view.adapter.SwipePagerAdapter;
import com.alsash.reciper.view.fragment.dialog.RecipeBottomDialog;

/**
 * The Activity that represents two tabs with RecipeCatPagerAdapter:
 * Tab one - RecipeLabelsFragment
 * Tab two - RecipeBookmarksFragment
 */
public class RecipeTabFavActivity extends RecipeTabActivity implements RecipeListInteraction {

    // Adapters
    private RecipeFavPagerAdapter pagerAdapter;

    @Override
    public void onExpand(Recipe recipe, int position) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe);
        bottomDialog.show(getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void onOpen(Recipe recipe, int position) {
        RecipeDetailActivity.start(RecipeTabFavActivity.this, recipe.getId());
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return R.id.drawer_base_nav_recipe_favorite;
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
