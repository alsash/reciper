package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeBackCardHolder;
import com.alsash.reciper.ui.adapter.holder.RecipeFrontCardHolder;
import com.alsash.reciper.ui.adapter.holder.RecipeListBaseHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListBaseHolder> {

    private final List<Recipe> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;
    private final Set<Integer> recipeBackCardViews = new HashSet<>();

    public RecipeListAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add(new RecipeImpl(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (recipeBackCardViews.contains(position)) {
            return R.layout.card_recipe_back;
        } else {
            return R.layout.card_recipe_front;
        }
    }

    @Override
    public RecipeListBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType) {
            case R.layout.card_recipe_front:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_recipe_front, parent, false);
                return new RecipeFrontCardHolder(itemView);
            case R.layout.card_recipe_back:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_recipe_back, parent, false);
                return new RecipeBackCardHolder(itemView);
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecipeListBaseHolder holder, int position) {
        holder.bindRecipe(recipeList.get(position));
        for (ActionViewEntry actionView : holder.getActionViews()) {
            switch (actionView.action) {
                case OPEN:
                    actionView.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                case EXPAND:
                    actionView.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                case FLIP:
                    actionView.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            }
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public enum Action {
        OPEN, EXPAND, FLIP
    }

    public interface OnRecipeInteraction {

        void click(Recipe recipe);

        void expand(Recipe recipe);
    }

    private static class RecipeImpl implements Recipe {

        private final long id;

        public RecipeImpl(long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return "Recipe # " + id;
        }
    }

    public static class ActionViewEntry {
        public final Action action;
        public final View view;

        public ActionViewEntry(Action action, View view) {
            this.action = action;
            this.view = view;
        }
    }
}
