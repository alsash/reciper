package com.alsash.reciper.mvp.model.entity;

import java.util.List;

/**
 * A Recipe entity with to-many relations
 */
public interface RecipeFull extends Recipe {

    List<Photo> getPhotos();

    List<Label> getLabels();

    List<Ingredient> getIngredients();

    List<Method> getMethods();
}
