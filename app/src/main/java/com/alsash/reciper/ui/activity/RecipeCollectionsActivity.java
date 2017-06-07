package com.alsash.reciper.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.alsash.reciper.ui.fragment.RecipeTabCategoryFragment;
import com.alsash.reciper.ui.fragment.RecipeTabGridFragment;
import com.alsash.reciper.ui.fragment.RecipeTabLabelFragment;
import com.alsash.reciper.ui.view.SwipeViewPager;

public class RecipeCollectionsActivity extends AppCompatActivity {

    private SwipeViewPager pager;

    private SwipePagerAdapter adapter = new SwipePagerAdapter(getSupportFragmentManager(),
            new Fragment[]{
                    RecipeTabCategoryFragment.newInstance(),
                    RecipeTabGridFragment.newInstance(),
                    RecipeTabLabelFragment.newInstance(),
            });

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.activity_recipe_collections_nav_categories:
                    pager.setCurrentItem(0);
                    return true;
                case R.id.activity_recipe_collections_nav_grid:
                    pager.setCurrentItem(1);
                    return true;
                case R.id.activity_recipe_collections_nav_labels:
                    pager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_collections);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(R.string.activity_recipe_collections_title);
        pager = ((SwipeViewPager) findViewById(R.id.activity_recipe_collections_pager));
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        ((BottomNavigationView) findViewById(R.id.activity_recipe_collections_nav))
                .setOnNavigationItemSelectedListener(navigationListener);
    }
}
