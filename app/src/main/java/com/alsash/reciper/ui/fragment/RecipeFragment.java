package com.alsash.reciper.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alsash.reciper.data.model.Recipe;

public class RecipeFragment extends Fragment {

    private static final String TAG = RecipeFragment.class.getCanonicalName();

    private static final String ARG_RECIPE_ID = TAG + ".arg_recipe_id";

    public static RecipeFragment newInstance() {
        return newInstance(null);
    }

    public static RecipeFragment newInstance(Recipe recipe) {

        RecipeFragment fragment = new RecipeFragment();
        if (recipe != null) {
            Bundle args = new Bundle();
            args.putLong(ARG_RECIPE_ID, recipe.getId());
            fragment.setArguments(args);
        }
        return fragment;
    }
}
