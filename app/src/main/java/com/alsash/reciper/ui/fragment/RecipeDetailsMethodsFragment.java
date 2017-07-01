package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.presenter.RecipeDetailsMethodsPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsMethodsView;
import com.alsash.reciper.ui.adapter.MethodListAdapter;
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
    private RecyclerView methodsList;
    private MethodListAdapter methodsAdapter;

    public static RecipeDetailsMethodsFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsMethodsFragment(), intent);
    }

    @Override
    public void onMethodEdit(Method method, String newBody) {
        presenter.onMethodEdit(method, newBody);
    }

    @Override
    public void showMethods(List<Method> methods) {
        methodsTitle.setText(getResources().getQuantityString(R.plurals.quantity_method,
                methods.size(), methods.size()));
        methodsAdapter = new MethodListAdapter(this, methods);
        methodsList.setAdapter(methodsAdapter);
        methodsList.setNestedScrollingEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_methods, container, false);
        methodsTitle = (TextView) layout.findViewById(R.id.recipe_methods_title);
        methodsAdd = (ImageButton) layout.findViewById(R.id.recipe_methods_add);
        methodsList = (RecyclerView) layout.findViewById(R.id.recipe_methods_list);
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
}
