package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.presenter.RecipeDetailsMethodsPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsMethodsView;
import com.alsash.reciper.ui.adapter.MethodListAdapter;
import com.alsash.reciper.ui.adapter.holder.DragAndDropHolder;
import com.alsash.reciper.ui.adapter.interaction.MethodInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent recipe methods
 */
public class RecipeDetailsMethodsFragment extends BaseFragment<RecipeDetailsMethodsView>
        implements RecipeDetailsMethodsView, MethodInteraction {

    @Inject
    RecipeDetailsMethodsPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private TextView methodsTitle;
    private ImageButton methodsAdd;
    private View.OnClickListener methodAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onMethodAdd(getThisView());
        }
    };
    private RecyclerView methodsList;
    private MethodListAdapter methodsAdapter;
    private ItemTouchHelper.Callback methodsTouchCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START) {
        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder source,
                              RecyclerView.ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType()) return false;
            presenter.onMethodsMove(getThisView(),
                    source.getAdapterPosition(),
                    target.getAdapterPosition());
            if (source instanceof DragAndDropHolder)
                ((DragAndDropHolder) source).onMoveAtDragAndDrop();
            if (target instanceof DragAndDropHolder)
                ((DragAndDropHolder) target).onMoveAtDragAndDrop();
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
            presenter.onMethodDelete(getThisView(), holder.getAdapterPosition());
        }
    };
    private ItemTouchHelper methodsTouchHelper = new ItemTouchHelper(methodsTouchCallback);

    public static RecipeDetailsMethodsFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsMethodsFragment(), intent);
    }

    @Override
    public void onMethodEdit(Method method, String newBody) {
        presenter.onMethodEdit(method, newBody);
    }

    @Override
    public void onMethodDrag(RecyclerView.ViewHolder holder) {
        methodsTouchHelper.startDrag(holder);
    }

    @Override
    public void showMethods(List<Method> methods) {
        if (methodsAdapter != null && methodsList != null) {
            methodsAdapter.notifyDataSetChanged();
        } else {
            methodsAdapter = new MethodListAdapter(this, methods);
            if (methodsList != null) methodsList.setAdapter(methodsAdapter);
        }
        showMethodsTitle();
    }

    @Override
    public void showMethodInsert(int position) {
        if (methodsAdapter != null) {
            methodsAdapter.notifyItemInserted(position);
            int size = methodsAdapter.getItemCount();
            if (size > 1 && position < size) {
                if (position + 1 < size) methodsAdapter.notifyItemRangeChanged(position + 1, size);
                if (position > 0) methodsAdapter.notifyItemRangeChanged(0, position);
            }
            showMethodsTitle();
        }
    }

    @Override
    public void showMethodDelete(int position) {
        if (methodsAdapter != null) {
            methodsAdapter.notifyItemRemoved(position);
            int size = methodsAdapter.getItemCount();
            if (size > 1 && position < size) {
                methodsAdapter.notifyItemRangeChanged(position, size);
                if (position > 1) methodsAdapter.notifyItemRangeChanged(0, position - 1);
            }
            showMethodsTitle();
        }
    }

    @Override
    public void showMethodDeleteMessage(int position, final MutableBoolean reject) {
        if (getView() == null) {
            reject.set(false);
            return;
        }
        Snackbar
                .make(getView(),
                        getString(R.string.fragment_recipe_details_methods_delete_action,
                                position + 1),
                        Snackbar.LENGTH_LONG)
                .setAction(R.string.fragment_recipe_details_methods_delete_reject,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reject.set(true);
                            }
                        })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!reject.is()) {
                            reject.set(false);
                        }
                    }
                })
                .setActionTextColor(ResourcesCompat.getColor(getResources(),
                        R.color.orange_500, null))
                .show();
    }

    @Override
    public void showMethodMove(int fromPosition, int toPosition) {
        if (methodsAdapter != null)
            methodsAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_methods, container, false);
        methodsTitle = (TextView) layout.findViewById(R.id.recipe_methods_title);
        methodsAdd = (ImageButton) layout.findViewById(R.id.recipe_methods_add);
        methodsAdd.setOnClickListener(methodAddListener);
        methodsList = (RecyclerView) layout.findViewById(R.id.recipe_methods_list);
        if (methodsAdapter != null) {
            methodsList.setAdapter(methodsAdapter);
            showMethodsTitle();
        }
        methodsList.setNestedScrollingEnabled(false);
        methodsTouchHelper.attachToRecyclerView(methodsList);
        return layout;
    }

    @Override
    protected RecipeDetailsMethodsPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }

    private void showMethodsTitle() {
        if (methodsTitle != null && methodsAdapter != null) {
            int size = methodsAdapter.getItemCount();
            methodsTitle.setText(getResources().getQuantityString(R.plurals.quantity_method,
                    size, size));
        }
    }
}
