package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.BaseRecipesView;
import com.alsash.reciper.ui.activity.RecipeDetailActivity;
import com.alsash.reciper.ui.fragment.dialog.RecipeBottomDialog;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.delegate.FragmentMvpDelegate;
import com.hannesdorfmann.mosby3.mvp.delegate.FragmentMvpDelegateImpl;

public abstract class BaseRecipesFragment<V extends BaseRecipesView, P extends MvpPresenter<V>>
        extends MvpFragment<V, P>
        implements BaseRecipesView {

    private RecyclerView recyclerView;

    protected abstract void setupList(RecyclerView list);

    @Override
    public void showDetails(Recipe recipe) {
        RecipeBottomDialog bottomDialog = RecipeBottomDialog.newInstance(recipe.getId());
        bottomDialog.show(getActivity().getSupportFragmentManager(), bottomDialog.getTag());
    }

    @Override
    public void showRecipe(Recipe recipe) {
        RecipeDetailActivity.start(getContext(), recipe.getId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_list, container, false);
        bindViews(layout);
        setupList(recyclerView);
        return layout;
    }

    private void bindViews(View layout) {
        recyclerView = (RecyclerView) layout.findViewById(R.id.list);
    }

    @NonNull
    @Override
    protected FragmentMvpDelegate<V, P> getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new FragmentMvpDelegateImpl<>(this, this, false, false);
        }
        return mvpDelegate;
    }
}
