package com.alsash.reciper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.Recipe;
import com.alsash.reciper.ui.animator.DepthPageTransformer;
import com.alsash.reciper.ui.contract.KeyContract;
import com.alsash.reciper.ui.fragment.RecipeDetailMainFragment;
import com.alsash.reciper.ui.vector.VectorHelper;

public class RecipeDetailActivity extends BaseDrawerActivity {

    // Model
    private Recipe recipe;

    // Views
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager pager;
    private FloatingActionButton fab;

    private TextView peakWeight;
    private TextView peakServing;
    private NumberPicker weightPicker;
    private NumberPicker servingPicker;

    public static void start(Context context, long recipeId) {
        Intent starter = new Intent(context, RecipeDetailActivity.class);
        starter.putExtra(KeyContract.KEY_RECIPE_ID, recipeId);
        context.startActivity(starter);
    }

    @Nullable
    @Override
    protected Integer getNavItemId() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        bindModel();
        bindViews();
        setupToolbar();
        setupPager();
        setupTabs();
        setupBottom();
        setupFab();
        setupList();
    }

    private void bindModel() {
        long id = getIntent().getLongExtra(KeyContract.KEY_RECIPE_ID, -1);
        // recipe = RecipeManager.getInstance().search(id);
        assert recipe != null;
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_detail_toolbar);
        tabs = (TabLayout) findViewById(R.id.activity_recipe_detail_tabs);
        pager = (ViewPager) findViewById(R.id.activity_recipe_detail_pager);
        fab = (FloatingActionButton) findViewById(R.id.activity_recipe_detail_fab);

        peakWeight = (TextView) findViewById(R.id.bottom_recipe_detail_weight);
        peakServing = (TextView) findViewById(R.id.bottom_recipe_detail_serving);
        weightPicker = (NumberPicker) findViewById(R.id.bottom_recipe_detail_weight_picker);
        servingPicker = (NumberPicker) findViewById(R.id.bottom_recipe_detail_serving_picker);
    }

    private void setupToolbar() {
        toolbar.setTitle(recipe.getName());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true); // Back arrow on toolbar
        setupDrawer(null); // Parent navigation drawer without button on toolbar
    }

    private void setupPager() {
        pager.setAdapter(new TabAdapter(getSupportFragmentManager(), recipe.getId()));
        pager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void setupTabs() {
        tabs.setupWithViewPager(pager);
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

    public static class TabAdapter extends FragmentPagerAdapter {

        private long recipeId;

        public TabAdapter(FragmentManager fm, long recipeId) {
            super(fm);
            this.recipeId = recipeId;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RecipeDetailMainFragment.newInstance(recipeId);
                default:
                    return new DetailFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "PAGE " + String.valueOf(position + 1);
        }
    }

    public static class DetailFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_scrolling_text, container, false);
        }
    }
}
