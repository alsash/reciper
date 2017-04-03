package com.alsash.reciper.mvp.model;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by aldolyna on 4/4/17.
 */

public interface Category extends RecipeGroup {
    @Override
    @Nullable
    List<Recipe> getRecipes();
}
