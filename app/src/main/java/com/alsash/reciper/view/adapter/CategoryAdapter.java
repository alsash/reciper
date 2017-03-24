package com.alsash.reciper.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.model.models.Category;
import com.alsash.reciper.model.models.Recipe;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.adapter.holder.CategoryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

    private RecipeListInteraction recipeInteraction;
    private Map<Category, List<Recipe>> categoryRecipesMap;
    private List<Category> categories = new ArrayList<>();

    public CategoryAdapter(RecipeListInteraction recipeInteraction,
                           Map<Category, List<Recipe>> categoryRecipesMap) {
        this.recipeInteraction = recipeInteraction;
        this.categoryRecipesMap = categoryRecipesMap;
        this.categories.addAll(categoryRecipesMap.keySet());
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = categories.get(position);
        List<Recipe> recipes = categoryRecipesMap.get(category);
        holder.bindCategory(category, recipes, recipeInteraction);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
