package com.alsash.reciper.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.presenter.entity.Category;
import com.alsash.reciper.presenter.interaction.RecipeListInteraction;
import com.alsash.reciper.view.adapter.holder.CategoryHolder;

import java.util.List;

public class RecipeGroupCardAdapter extends RecyclerView.Adapter<CategoryHolder> {

    private RecipeListInteraction recipeInteraction;
    private List<Category> categories;

    public RecipeGroupCardAdapter(RecipeListInteraction recipeInteraction,
                                  List<Category> categories) {
        this.recipeInteraction = recipeInteraction;
        this.categories = categories;
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
        holder.bindCategory(category, recipeInteraction);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
