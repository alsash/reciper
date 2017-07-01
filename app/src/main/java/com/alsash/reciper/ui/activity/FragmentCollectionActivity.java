package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.FragmentCollectionPresenter;
import com.alsash.reciper.mvp.view.FragmentCollectionView;
import com.alsash.reciper.ui.adapter.SwipePagerAdapter;
import com.alsash.reciper.ui.view.SwipeViewPager;

import javax.inject.Inject;

public class FragmentCollectionActivity extends BaseDrawerActivity<FragmentCollectionView>
        implements FragmentCollectionView {

    @Inject
    FragmentCollectionPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private BottomNavigationView bottomNav;
    private SwipePagerAdapter adapter;
    private SwipeViewPager pager;
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return presenter.onBottomNavigationPressed(getThisView(), item.getItemId());
                }
            };

    public void showCollections(Fragment[] collections) {
        if (adapter == null) {
            adapter = new SwipePagerAdapter(getSupportFragmentManager(), collections);
        } else {
            adapter.setFragments(collections);
        }
    }

    @Override
    public void hideBottomNavigation(boolean hide) {
        bottomNav.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showBottomNavigation(@IdRes int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }

    @Override
    public void showCollection(int position) {
        pager.setCurrentItem(position);
    }

    @Override
    public int shownCollection() {
        return pager.getCurrentItem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenter.setCollections(navigator.getFragmentCollection(getIntent()));
        presenter.refresh(getThisView());
        presenter.visible(getThisView());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_collection);
        setSupportActionBar((Toolbar) findViewById(R.id.activity_fragment_collection_toolbar));
        pager = ((SwipeViewPager) findViewById(R.id.activity_fragment_collection_pager));
        pager.setAdapter(adapter);
        bottomNav = (BottomNavigationView) findViewById(R.id.activity_fragment_collection_nav);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);
    }

    @Override
    protected BasePresenter<FragmentCollectionView> inject() {
        ((ReciperApp) getApplicationContext())
                .getUiStartComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setCollections(navigator.getFragmentCollection(getIntent()));
    }

    @Override
    protected NavigationLogic getNavigationLogic() {
        return navigator;
    }
}
