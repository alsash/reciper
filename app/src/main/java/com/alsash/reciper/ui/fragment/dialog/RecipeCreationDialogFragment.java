package com.alsash.reciper.ui.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.RecipeCreationPresenter;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.mvp.view.RecipeCreationView;
import com.alsash.reciper.ui.adapter.EntitySelectionAdapter;
import com.alsash.reciper.ui.adapter.interaction.EntitySelectionInteraction;
import com.alsash.reciper.ui.view.helper.RecyclerViewHelper;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Complex creation dialog
 */
public class RecipeCreationDialogFragment extends AppCompatDialogFragment
        implements RecipeCreationView, EntitySelectionInteraction {

    @Inject
    RecipeCreationPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private RecyclerView authorList;
    private RecyclerView categoryList;

    private MutableBoolean visibleHelper;
    private AdapterHolder<Category> categoryAdapterHolder;
    private AdapterHolder<Author> authorAdapterHolder;

    public static void start(FragmentManager fragmentManager) {
        RecipeCreationDialogFragment fragment = new RecipeCreationDialogFragment();
        fragment.show(fragmentManager, "Tag");
    }

    @Override
    public void onSelect(BaseEntity entity) {
        presenter.onSelect(entity);
    }

    @Override
    public boolean isViewVisible() {
        return visibleHelper.is();
    }

    @Override
    public BaseListView<Category> getCategoriesView() {
        return categoryAdapterHolder;
    }

    @Override
    public BaseListView<Author> getAuthorsView() {
        return authorAdapterHolder;
    }

    @Override
    public void openRecipe(Recipe recipe) {
        navigator.fromActivity(getActivity()).toRecipeDetailsView(recipe);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_creation_dialog, container, false);

        authorList = (RecyclerView) layout.findViewById(R.id.recipe_creation_author_list);
        authorList.setAdapter(authorAdapterHolder.getAdapter());
        authorList.addOnScrollListener(authorAdapterHolder.getPaginationListener());

        categoryList = (RecyclerView) layout.findViewById(R.id.recipe_creation_category_list);
        categoryList.setAdapter(categoryAdapterHolder.getAdapter());
        categoryList.addOnScrollListener(categoryAdapterHolder.getPaginationListener());
        return layout;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(onCreateView(LayoutInflater.from(getActivity()), null, null))
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // builder.setPositiveButton(new )
    }

    @Override
    public void onAttach(Context context) {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiRecipeCreationComponent()
                .inject(this);
        visibleHelper = new MutableBoolean();
        categoryAdapterHolder = new AdapterHolder<>(Category.class, this, visibleHelper);
        authorAdapterHolder = new AdapterHolder<>(Author.class, this, visibleHelper);
        presenter.attach(this);
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        visibleHelper.set(true);
        presenter.visible(this);
    }

    @Override
    public void onStop() {
        visibleHelper.set(false);
        presenter.invisible(this);
        super.onStop();
    }

    @Override
    public void onDetach() {
        //  presenter.detach();
        super.onDetach();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        visibleHelper.set(false);
        presenter.invisible(this);
        presenter.detach();
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        visibleHelper.set(false);
        presenter.invisible(this);
        presenter.detach();
        super.onCancel(dialog);
    }

    private static class AdapterHolder<M extends BaseEntity> implements BaseListView<M> {

        private final Class<?> entityClass;
        private final EntitySelectionInteraction interaction;
        private final MutableBoolean visibleHelper;
        private boolean needPagination;
        private RecyclerView.Adapter adapter;
        private RecipeCreationPresenter presenter;
        private final RecyclerView.OnScrollListener paginationListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                if (!needPagination || newState != SCROLL_STATE_SETTLING) return;
                int lastPosition = RecyclerViewHelper.getLastVisibleItemPosition(rv.getLayoutManager());
                presenter.nextPagination(entityClass, lastPosition);
            }
        };

        public AdapterHolder(Class<?> entityClass,
                             EntitySelectionInteraction interaction,
                             MutableBoolean visibleHelper) {
            this.entityClass = entityClass;
            this.interaction = interaction;
            this.visibleHelper = visibleHelper;
        }

        public void setPresenter(RecipeCreationPresenter presenter) {
            this.presenter = presenter;
        }

        public RecyclerView.Adapter getAdapter() {
            return adapter;
        }

        public RecyclerView.OnScrollListener getPaginationListener() {
            return paginationListener;
        }

        public boolean isViewVisible() {
            return visibleHelper.is();
        }

        @Override
        public void setContainer(List<M> container) {
            adapter = new EntitySelectionAdapter(interaction, container, entityClass);
        }

        @Override
        public void setPagination(boolean needPagination) {
            this.needPagination = needPagination;
        }

        @Override
        public void showLoading(boolean loading) {
            // do nothing
        }

        @Override
        public void showInsert(int position) {
            adapter.notifyItemInserted(position);
        }

        @Override
        public void showInsert(int insertPosition, int insertCount) {
            adapter.notifyItemRangeInserted(insertPosition, insertCount);
        }

        @Override
        public void showUpdate(int position) {
            adapter.notifyItemChanged(position);
        }

        @Override
        public void showDelete(int position) {
            adapter.notifyItemRemoved(position);
        }
    }
}
