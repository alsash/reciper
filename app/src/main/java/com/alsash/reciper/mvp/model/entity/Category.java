package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A Category model
 */
public class Category extends BaseRecipeGroup {

    Category() {
    }

    Category(Long id,
             UUID uuid,
             String name,
             Date creationDate,
             Date changeDate,
             List<Recipe> recipes) {
        super(id, uuid, name, creationDate, changeDate, recipes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return uuid != null ? uuid.equals(category.uuid) : category.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
