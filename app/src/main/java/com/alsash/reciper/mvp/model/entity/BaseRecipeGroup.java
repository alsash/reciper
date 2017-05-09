package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A Model of composite entity group, that holds List of Recipes
 */
public abstract class BaseRecipeGroup extends BaseEntity {

    List<Recipe> recipes;

    protected BaseRecipeGroup() {
    }

    protected BaseRecipeGroup(Long id,
                              UUID uuid,
                              String name,
                              Date creationDate,
                              Date changeDate,
                              List<Recipe> recipes) {
        super(id, uuid, name, creationDate, changeDate);
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
