package com.alsash.reciper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.data.RecipeManager;
import com.alsash.reciper.data.model.Recipe;

public class RecipeDetailActivity extends BaseDrawerActivity {

    private static final String TAG = RecipeDetailActivity.class.getCanonicalName();

    private static final String EXTRA_RECIPE_ID = TAG + "extra_recipe_id";

    private Toolbar toolbar;
    private FloatingActionButton photoFab;
    private RecyclerView list;

    private Recipe recipe;

    public static void start(Context context, long recipeId) {
        Intent starter = new Intent(context, RecipeDetailActivity.class);
        starter.putExtra(EXTRA_RECIPE_ID, recipeId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        bindViews();
        setupFab();
        setupList();
        bindRecipe();
        setupToolbar();
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_detail_toolbar);
        list = (RecyclerView) findViewById(R.id.activity_recipe_detail_rv);
        photoFab = (FloatingActionButton) findViewById(R.id.activity_recipe_detail_fab_photo);
    }

    private void setupFab() {
        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupList() {
        // list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true); // Back arrow on toolbar
        setupDrawer(null); // Parent drawer without button on toolbar
    }

    private void bindRecipe() {
        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        recipe = RecipeManager.getInstance().getRecipe(id);
        assert recipe != null;
        toolbar.setTitle(recipe.getName());
    }
}
