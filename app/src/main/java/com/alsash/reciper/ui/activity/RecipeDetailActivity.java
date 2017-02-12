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
    private FloatingActionButton fab;
    private RecyclerView list;

    private Recipe recipe;
    private boolean isNewRecipe;

    public static Intent getStarter(Context context, long recipeId) {
        Intent starter = new Intent(context, RecipeDetailActivity.class);
        starter.putExtra(EXTRA_RECIPE_ID, recipeId);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        bindViews();
        bindRecipe();
        setupToolbar();
        setupFab();
        setupList();
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_detail_toolbar);
        list = (RecyclerView) findViewById(R.id.activity_recipe_detail_rv);
        fab = (FloatingActionButton) findViewById(R.id.activity_recipe_detail_fab);
    }

    private void bindRecipe() {
        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, RecipeManager.RECIPE_ID_UNKNOWN);
        if (id == RecipeManager.RECIPE_ID_UNKNOWN) {
            isNewRecipe = true;
        }
        recipe = RecipeManager.getInstance().getRecipe(id);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupDrawer(toolbar); // Parent
    }

    private void setupFab() {
        fab.setOnClickListener(new View.OnClickListener() {
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
}
