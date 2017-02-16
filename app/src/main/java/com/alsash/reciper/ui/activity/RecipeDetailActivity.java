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
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.data.RecipeManager;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.vector.VectorHelper;

public class RecipeDetailActivity extends BaseDrawerActivity {

    private static final String TAG = RecipeDetailActivity.class.getCanonicalName();

    private static final String EXTRA_RECIPE_ID = TAG + "extra_recipe_id";

    private Toolbar toolbar;
    private RecyclerView list;
    private FloatingActionButton photoFab;

    private TextView peakWeight;
    private TextView peakServing;
    private NumberPicker weightPicker;
    private NumberPicker servingPicker;

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
        setupBottom();
        setupFab();
        setupList();
        bindRecipe();
        setupToolbar();
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_detail_toolbar);
        list = (RecyclerView) findViewById(R.id.activity_recipe_detail_rv);
        photoFab = (FloatingActionButton) findViewById(R.id.activity_recipe_detail_fab_photo);

        peakWeight = (TextView) findViewById(R.id.bottom_recipe_detail_weight);
        peakServing = (TextView) findViewById(R.id.bottom_recipe_detail_serving);
        weightPicker = (NumberPicker) findViewById(R.id.bottom_recipe_detail_weight_picker);
        servingPicker = (NumberPicker) findViewById(R.id.bottom_recipe_detail_serving_picker);
    }

    private void setupBottom() {
        VectorHelper vector = new VectorHelper(this);
        vector.setCompoundDrawables(peakWeight, R.color.white, R.drawable.ic_weight);
        vector.setCompoundDrawables(peakServing, R.color.white, R.drawable.ic_serving);
        weightPicker.setMinValue(0);
        weightPicker.setMaxValue(1000);
        weightPicker.setValue(500);
        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                peakWeight.setText(getResources()
                        .getQuantityString(R.plurals.quantity_weight_gram, newVal, newVal));
            }
        });
        servingPicker.setMinValue(1);
        servingPicker.setMaxValue(10);
        servingPicker.setValue(2);
        servingPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                peakServing.setText(getResources()
                        .getQuantityString(R.plurals.quantity_serving, newVal, newVal));
            }
        });
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
