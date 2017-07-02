package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.presenter.EntityListPresenter;
import com.alsash.reciper.mvp.view.EntityListView;
import com.alsash.reciper.ui.adapter.EntityListAdapter;
import com.alsash.reciper.ui.adapter.interaction.EntityListInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent list of recipe cards
 */
public class EntityListFragment extends BaseListFragment<BaseEntity, EntityListView>
        implements EntityListView, EntityListInteraction {

    @Inject
    EntityListPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private Class<?> entityClass;

    public static EntityListFragment newInstance(Intent intent) {
        return getThisFragment(new EntityListFragment(), intent);
    }

    @Override
    public void onOpen(BaseEntity entity) {
        if (entity instanceof Category)
            navigator.fromActivity(getActivity()).toRecipeListView((Category) entity);
        if (entity instanceof Label)
            navigator.fromActivity(getActivity()).toRecipeListView((Label) entity);
        if (entity instanceof Food)
            navigator.fromActivity(getActivity()).toRecipeListView((Food) entity);
        if (entity instanceof Author)
            navigator.fromActivity(getActivity()).toRecipeListView((Author) entity);
    }

    @Override
    public void onEditValues(BaseEntity entity, String... values) {
        presenter.editValues(getThisView(), entity, values);
    }

    @Override
    public void onEditPhoto(BaseEntity entity) {

    }

    @Override
    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_entity_list_appbar, menu);
        menu.getItem(0).setVisible(entityClass.equals(Food.class));
        menu.getItem(0).setEnabled(entityClass.equals(Food.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_add:
                presenter.addEntity(getThisView());
                return true;
            case R.id.appbar_search:
                navigator.fromActivity(getActivity()).toFoodSearchView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected EntityListPresenter inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiEntityListComponent()
                .inject(this);
        return presenter.setEntityRestriction(navigator.getRestriction(getThisIntent(this)));
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<BaseEntity> container) {
        return new EntityListAdapter(this, container, entityClass);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
