package com.alsash.reciper.ui.adapter.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.adapter.RecipeSingleCardAdapter;
import com.alsash.reciper.ui.animator.FlipCardAnimator;
import com.alsash.reciper.ui.presenter.entity.Category;
import com.alsash.reciper.ui.presenter.interaction.RecipeListInteraction;

public class CategoryHolder extends RecyclerView.ViewHolder {

    private TextView categoryTitle;
    private RecyclerView categoryList;

    public CategoryHolder(View itemView) {
        super(itemView);
        categoryTitle = (TextView) itemView.findViewById(R.id.category_name);
        categoryList = (RecyclerView) itemView.findViewById(R.id.category_list);
    }

    public void bindCategory(Category category, RecipeListInteraction interaction) {
        categoryTitle.setText(category.getName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(categoryList.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(4);
        categoryList.setLayoutManager(layoutManager);
        categoryList.setAdapter(new RecipeSingleCardAdapter(interaction, category.getRecipes()));
        categoryList.setItemAnimator(new FlipCardAnimator());
    }
}
