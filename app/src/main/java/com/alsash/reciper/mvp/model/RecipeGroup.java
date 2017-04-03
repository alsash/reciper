package com.alsash.reciper.mvp.model;

import java.util.List;

/**
 * Created by aldolyna on 4/3/17.
 */

public interface RecipeGroup {

    Long getId();

    String getName();

    List<Recipe> getRecipes();
}
