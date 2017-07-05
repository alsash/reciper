package com.alsash.reciper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.model.tab.SwipeTab;
import com.alsash.reciper.mvp.presenter.RecipeDetailsPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.alsash.reciper.ui.animator.DepthPageTransformer;
import com.alsash.reciper.ui.fragment.dialog.SimpleDialog;
import com.alsash.reciper.ui.loader.ImageLoader;
import com.alsash.reciper.ui.view.SwipeViewPager;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class RecipeDetailsActivity extends BaseActivity<RecipeDetailsView>
        implements RecipeDetailsView {

    @Inject
    RecipeDetailsPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private TabLayout tabs;
    private SwipeViewPager pager;
    private SwipePagerAdapter adapter;
    private ImageView image;

    @Override
    public void setDetails(SwipeTab[] details) {
        if (adapter == null) {
            adapter = new SwipePagerAdapter(getSupportFragmentManager(), details);
        } else {
            adapter.setTabs(details);
        }
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
        if (toolbarLayout != null)
            toolbarLayout.setTitle((title == null || title.equals("")) ? " " : title);
    }

    @Override
    public void showNameEditDialog(Recipe recipe, MutableString listener) {
        SimpleDialog.showEditRecipeName(getThisContext(), recipe, listener);
    }

    @Override
    public void showPhoto(Photo photo) {
        ImageLoader.get().source(photo).load(image);
    }

    @Override
    public void showPhotoEditDialog(Photo photo, MutableString listener) {
        SimpleDialog.showEditPhotoUrl(getThisContext(), photo, listener);
    }

    @Override
    public void showConfirmDeleteDialog(String recipeName, MutableBoolean listener) {
        SimpleDialog.showConfirmDelete(getThisContext(), recipeName, listener);
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        bindViews();
        setupToolbar();
        setupPager();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        presenter.setRestriction(navigator.getRestriction(intent))
                .setFragments(navigator.getFragmentCollection(intent))
                .attach(getThisView());
        if (isViewVisible()) presenter.visible(getThisView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.finish(getThisView());
                return true;
            case R.id.appbar_edit_name:
                presenter.editRecipeName(getThisView());
                return true;
            case R.id.appbar_edit_photo:
                presenter.editRecipePhoto(getThisView());
                return true;
            case R.id.appbar_delete:
                presenter.deleteRecipe(getThisView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected RecipeDetailsPresenter inject() {
        ((ReciperApp) getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getIntent()))
                .setFragments(navigator.getFragmentCollection(getIntent()));
    }

    private void bindViews() {
        appbar = (AppBarLayout) findViewById(R.id.activity_recipe_details_appbar);
        toolbar = (Toolbar) findViewById(R.id.activity_recipe_details_toolbar);
        toolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.activity_recipe_details_toolbar_layout);
        tabs = (TabLayout) findViewById(R.id.activity_recipe_details_tabs);
        pager = (SwipeViewPager) findViewById(R.id.activity_recipe_details_pager);
        image = (ImageView) findViewById(R.id.activity_recipe_details_toolbar_image);
    }

    private void setupToolbar() {
        appbar.addOnOffsetChangedListener(new TabsColorChanger(this, tabs));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupPager() {
        pager.setAdapter(adapter);
        pager.setTabs(tabs);
        pager.setPageTransformer(true, new DepthPageTransformer());
    }

    private static class TabsColorChanger implements AppBarLayout.OnOffsetChangedListener {
        private WeakReference<TabLayout> tabsRef;
        private ColorStateList colorDark;
        private ColorStateList colorLight;
        private Boolean collapsed;

        public TabsColorChanger(Context context, TabLayout tabs) {
            tabsRef = new WeakReference<>(tabs);
            colorDark = AppCompatResources.getColorStateList(context,
                    R.color.cs_gray_def_600_sel_800);
            colorLight = AppCompatResources.getColorStateList(context,
                    R.color.cs_white_def_a080_sel_a100);
        }

        private static boolean isCollapse(int verticalOffset) {
            return verticalOffset != 0;
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (tabsRef.get() == null) return;
            if (isCollapsed() != null && (isCollapse(verticalOffset) == isCollapsed())) return;
            setCollapsed(isCollapse(verticalOffset));
            tabsRef.get().setTabTextColors(isCollapsed() ? colorDark : colorLight);
        }

        public Boolean isCollapsed() {
            return collapsed;
        }

        private void setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
        }
    }
}
