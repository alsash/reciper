package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.adapter.holder.RecipeCardHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardHolder> {

    private static final String CLASSNAME = RecipeCardAdapter.class.getCanonicalName();

    public static final String PAYLOAD_FLIP_FRONT_TO_BACK = CLASSNAME + ".payload_front_to_back";
    public static final String PAYLOAD_FLIP_BACK_TO_FRONT = CLASSNAME + ".payload_back_to_front";

    private final List<Recipe> recipeList = new ArrayList<>();
    private final OnRecipeInteraction recipeInteraction;
    private final Set<Integer> backCardPositions = new HashSet<>();

    public RecipeCardAdapter(OnRecipeInteraction recipeInteraction) {
        this.recipeInteraction = recipeInteraction;
        for (int i = 0; i < 30; i++) {
            recipeList.add(new RecipeImpl(i));
        }
    }

    @Override
    public RecipeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);
        return new RecipeCardHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RecipeCardHolder holder, int position) {

        holder.bindRecipe(recipeList.get(position));
        holder.setBackVisible(backCardPositions.contains(position));

        holder.setListeners(
                // Flip Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Flip animation. Stage 1 of 3. Notify observers about flip is triggered.
                        int adapterPosition = holder.getAdapterPosition();
                        if (backCardPositions.contains(adapterPosition)) {
                            backCardPositions.remove(adapterPosition);
                            notifyItemChanged(adapterPosition, PAYLOAD_FLIP_BACK_TO_FRONT);
                        } else {
                            backCardPositions.add(adapterPosition);
                            notifyItemChanged(adapterPosition, PAYLOAD_FLIP_FRONT_TO_BACK);
                        }
                    }
                },
                // Expand Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recipeInteraction.expand(recipeList.get(holder.getAdapterPosition()));
                    }
                },
                // Open Listener
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recipeInteraction.open(recipeList.get(holder.getAdapterPosition()));
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface OnRecipeInteraction {

        void open(Recipe recipe);

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
}
