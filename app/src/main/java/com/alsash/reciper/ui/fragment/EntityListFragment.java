package com.alsash.reciper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.presenter.EntityListPresenter;
import com.alsash.reciper.mvp.view.EntityListView;
import com.alsash.reciper.ui.adapter.EntityListAdapter;
import com.alsash.reciper.ui.adapter.interaction.EntityListInteraction;
import com.alsash.reciper.ui.fragment.dialog.SimpleDialog;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent list of entities
 */
public class EntityListFragment extends BaseListFragment<BaseEntity, EntityListView>
        implements EntityListView, EntityListInteraction {

    @Inject
    EntityListPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private Class<?> entityClass;
    private ItemTouchHelper.Callback entityTouchCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN, ItemTouchHelper.START) {

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder source,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
            presenter.deleteEntity(getThisView(), holder.getAdapterPosition());
        }
    };
    private ItemTouchHelper entityTouchHelper = new ItemTouchHelper(entityTouchCallback);


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
        presenter.editPhoto(getThisView(), entity);
    }

    @Override
    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void showDeleteSuccessMessage(String entityName, final MutableBoolean reject) {
        if (getView() == null) {
            reject.set(false);
            return;
        }
        Snackbar.make(getView(),
                getString(R.string.fragment_entity_list_delete_success, entityName),
                Snackbar.LENGTH_LONG)
                .setAction(R.string.fragment_entity_list_delete_reject,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reject.set(true);
                            }
                        })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event != BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION)
                            reject.set(false);
                    }
                })
                .setActionTextColor(ResourcesCompat.getColor(getResources(),
                        R.color.orange_500, null))
                .show();
    }

    @Override
    public void showDeleteFailMessage(String entityName, int recipesCount) {
        if (getView() == null) return;
        Snackbar.make(getView(),
                getString(R.string.fragment_entity_list_delete_fail, entityName,
                        getResources().getQuantityString(R.plurals.quantity_recipe, recipesCount,
                                recipesCount)),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showPhotoEditDialog(Photo photo, MutableString listener) {
        SimpleDialog.showEditPhotoUrl(getActivity(), photo, listener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(entityClass.equals(Food.class) ?
                        R.menu.appbar_add_search :
                        R.menu.appbar_add,
                menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_add:
                presenter.addEntity(getThisView());
                return true;
            case R.id.appbar_search:
                Toast.makeText(getContext(), R.string.fragment_entity_list_food_search,
                        Toast.LENGTH_SHORT)
                        .show();
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
    protected void setupList() {
        super.setupList();
        entityTouchHelper.attachToRecyclerView(list);
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
