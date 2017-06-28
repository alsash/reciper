package com.alsash.reciper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.presenter.RecipeDetailsIngredientsPresenter;
import com.alsash.reciper.mvp.view.RecipeDetailsIngredientsView;
import com.alsash.reciper.ui.adapter.IngredientListAdapter;
import com.alsash.reciper.ui.adapter.interaction.IngredientInteraction;

import java.util.List;

import javax.inject.Inject;

/**
 * A fragment that represent recipe ingredients
 */
public class RecipeDetailsIngredientsFragment extends BaseFragment<RecipeDetailsIngredientsView>
        implements RecipeDetailsIngredientsView, IngredientInteraction {

    @Inject
    RecipeDetailsIngredientsPresenter presenter;
    @Inject
    NavigationLogic navigator;

    private TextView ingredientsTitle;
    private ImageButton ingredientsAdd;
    private RecyclerView ingredientsList;
    private IngredientListAdapter ingredientAdapter;

    public static RecipeDetailsIngredientsFragment newInstance(Intent intent) {
        return getThisFragment(new RecipeDetailsIngredientsFragment(), intent);
    }

    @Override
    public void onRequestName(Ingredient ingredient) {

    }

    @Override
    public void onRequestFood(Ingredient ingredient) {

    }

    @Override
    public void onEditWeight(Ingredient ingredient, int weight) {

    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientsTitle.setText(getResources().getQuantityString(R.plurals.quantity_ingredient,
                ingredients.size(), ingredients.size()));
        ingredientAdapter = new IngredientListAdapter(this, ingredients);
        ingredientsList.setAdapter(ingredientAdapter);
        ingredientsList.setNestedScrollingEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_details_ingredients,
                container, false);
        // Ingredients card
        ingredientsTitle = (TextView) layout.findViewById(R.id.recipe_ingredients_title);
        ingredientsAdd = (ImageButton) layout.findViewById(R.id.recipe_ingredients_add);
        ingredientsList = (RecyclerView) layout.findViewById(R.id.recipe_ingredients_list);
        ingredientsList.addItemDecoration(new DividerItemDecoration(ingredientsList.getContext(),
                DividerItemDecoration.VERTICAL));
        return layout;
    }

    @Override
    protected RecipeDetailsIngredientsPresenter inject() {
        ((ReciperApp) getContext().getApplicationContext())
                .getUiRecipeDetailsComponent()
                .inject(this);
        // Presenter will be embedded in the activity lifecycle
        return presenter.setRestriction(navigator.getRestriction(getThisIntent(this)));
    }
}
