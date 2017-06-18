package com.alsash.reciper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeDetailsPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.alsash.reciper.ui.animator.DepthPageTransformer;
import com.alsash.reciper.ui.loader.ImageLoader;
import com.alsash.reciper.ui.view.SwipeViewPager;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class RecipeDetailsActivity extends BaseActivity<RecipeDetailsView>
        implements RecipeDetailsView {

    @Inject
    RecipeDetailsPresenter presenter;
    @Inject
    AppNavigator navigator;

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TabLayout tabs;
    private SwipeViewPager pager;
    private SwipePagerAdapter adapter;
    private ImageView image;

    private TextView peakWeight;
    private TextView peakServing;
    private NumberPicker weightPicker;
    private NumberPicker servingPicker;

    @Override
    public void setDetails(SwipeTab[] details) {
        if (adapter != null) return;
        adapter = new SwipePagerAdapter(getSupportFragmentManager(), details);
    }

    @Override
    public void showDetail(int position) {
        if (pager == null) return;
        if (pager.getCurrentItem() != position) pager.setCurrentItem(position, true);
    }

    @Override
    public int shownDetail() {
        if (pager == null) return 0;
        return pager.getCurrentItem();
    }

    @Override
    public void showTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void showPhoto(Photo photo) {
        ImageLoader.get().source(photo).load(image);
    }

    @Override
    protected BasePresenter<RecipeDetailsView> inject() {
        ((ReciperApp) getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getIntent()))
                .setFragments(navigator.getFragmentCollections(getIntent()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        presenter.setRestriction(navigator.getRestriction(intent))
                .setFragments(navigator.getFragmentCollections(intent));
        presenter.attach(getThisView());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        bindViews();
        setupToolbar();
        setupPager();
    }

    private void bindViews() {
        appbar = (AppBarLayout) findViewById(R.id.activity_recipe_details_appbar);
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_details_toolbar);
        tabs = (TabLayout) findViewById(R.id.activity_recipe_details_tabs);
        pager = (SwipeViewPager) findViewById(R.id.activity_recipe_details_pager);
        image = (ImageView) findViewById(R.id.activity_recipe_details_toolbar_image);
//        toolbar = (Toolbar) findViewById(R.id.activity_recipe_details_toolbar);
//        tabs = (TabLayout) findViewById(R.id.activity_recipe_details_tabs);
//        pager = (ViewPager) findViewById(R.id.activity_recipe_details_pager);
//        peakWeight = (TextView) findViewById(R.id.bottom_recipe_detail_weight);
//        peakServing = (TextView) findViewById(R.id.bottom_recipe_detail_serving);
//        weightPicker = (NumberPicker) findViewById(R.id.bottom_recipe_detail_weight_picker);
//        servingPicker = (NumberPicker) findViewById(R.id.bottom_recipe_detail_serving_picker);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        appbar.addOnOffsetChangedListener(new TabColorListener(this, tabs));
    }

    private void setupPager() {
        pager.setAdapter(adapter);
        pager.setTabs(tabs);
        pager.setPageTransformer(true, new DepthPageTransformer());
    }

/*    private void setupBottom() {
        VectorHelper vector = new VectorHelper(this);
        vector.setCompoundDrawables(peakWeight, R.colorDark.white, R.drawable.ic_weight);
        vector.setCompoundDrawables(peakServing, R.colorDark.white, R.drawable.ic_serving);
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
        fab.setOnClickListener(new IView.OnClickListener() {
            @Override
            public void onClick(IView view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setActionSubject("Action", null).show();
            }
        });
    }*/

    private static class TabColorListener implements AppBarLayout.OnOffsetChangedListener {
        private WeakReference<TabLayout> tabsRef;
        private ColorStateList colorDark;
        private ColorStateList colorLight;
        private boolean collapsed;

        public TabColorListener(Context context, TabLayout tabs) {
            tabsRef = new WeakReference<>(tabs);
            colorDark = AppCompatResources.getColorStateList(context,
                    R.color.activity_recipe_details_tab_dark_bg_light);
            colorLight = AppCompatResources.getColorStateList(context,
                    R.color.activity_recipe_details_tab_light_bg_dark);
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
/*            if ((collapse(verticalOffset) && collapsed())
                    || (!collapse(verticalOffset) && !collapsed()))
                return;*/
            setCollapsed(verticalOffset);
            tabsRef.get().setTabTextColors(collapsed() ? colorDark : colorLight);
        }

        private void setCollapsed(int verticalOffset) {
            collapsed = collapse(verticalOffset);
        }

        private boolean collapsed() {
            return collapsed;
        }

        private boolean collapse(int verticalOffset) {
            return verticalOffset != 0;
        }
    }

}
