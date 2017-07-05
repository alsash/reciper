package com.alsash.reciper.ui.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.presenter.BasePresenter;
import com.alsash.reciper.mvp.presenter.RecipeCreationDialogPresenter;
import com.alsash.reciper.mvp.view.BaseListView;
import com.alsash.reciper.mvp.view.RecipeCreationDialogView;
import com.alsash.reciper.ui.adapter.EntitySelectionAdapter;
import com.alsash.reciper.ui.adapter.interaction.EntitySelectionInteraction;
import com.alsash.reciper.ui.view.helper.RecyclerViewHelper;

import java.util.List;

import javax.inject.Inject;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Complex creation dialog
 */
public class RecipeCreationDialogFragment extends BaseDialogFragment<RecipeCreationDialogView>
        implements RecipeCreationDialogView, EntitySelectionInteraction {

    @Inject
    RecipeCreationDialogPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private EditText recipeName;
    private RecyclerView authorList;
    private RecyclerView categoryList;

    private AdapterHolder<Category> categoryAdapterHolder;
    private AdapterHolder<Author> authorAdapterHolder;

    public static void start(FragmentManager fragmentManager) {
        RecipeCreationDialogFragment fragment = new RecipeCreationDialogFragment();
        fragment.show(fragmentManager, fragment.getTag());
    }

    @Override
    public void onSelect(BaseEntity entity) {
        presenter.onSelect(entity);
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

    @Override
    public void showError() {
        Toast.makeText(getContext(),
                getString(R.string.fragment_recipe_creation_dialog_error),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishView() {
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_creation_dialog, container, false);

        recipeName = (EditText) layout.findViewById(R.id.recipe_creation_dialog_name);

        authorList = (RecyclerView) layout.findViewById(R.id.recipe_creation_author_list);
        authorList.setAdapter(authorAdapterHolder.getAdapter());
        authorList.addOnScrollListener(authorAdapterHolder.getPaginationListener());

        categoryList = (RecyclerView) layout.findViewById(R.id.recipe_creation_category_list);
        categoryList.setAdapter(categoryAdapterHolder.getAdapter());
        categoryList.addOnScrollListener(categoryAdapterHolder.getPaginationListener());
        return layout;
    }

    @Override
    protected BasePresenter<RecipeCreationDialogView> inject() {
        ((ReciperApp) getActivity().getApplicationContext())
                .getUiDialogComponent()
                .inject(this);
        categoryAdapterHolder = new AdapterHolder<>(Category.class, this, visible);
        authorAdapterHolder = new AdapterHolder<>(Author.class, this, visible);
        categoryAdapterHolder.setPresenter(presenter);
        authorAdapterHolder.setPresenter(presenter);
        return presenter;
    }

    protected void clickPositive() {
        presenter.onCreationApprove(getThisView(), recipeName.getText().toString());
    }

    protected void clickNegative() {
        dismiss();
    }

    private static class AdapterHolder<M extends BaseEntity> implements BaseListView<M> {

        private final Class<?> entityClass;
        private final EntitySelectionInteraction interaction;
        private final MutableBoolean visibleHelper;
        private boolean needPagination;
        private RecyclerView.Adapter adapter;
        private RecipeCreationDialogPresenter presenter;
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

        public void setPresenter(RecipeCreationDialogPresenter presenter) {
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
