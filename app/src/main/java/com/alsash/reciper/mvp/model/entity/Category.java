package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A Category model
 */
public class Category extends BaseRecipeGroup {

    Photo photo;

    Category() {
    }

    Category(Long id,
             UUID uuid,
             String name,
             Date creationDate,
             Date changeDate,
             List<Recipe> recipes,
             Photo photo) {
        super(id, uuid, name, creationDate, changeDate, recipes);
        this.photo = photo;
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

    @Override
    int getSize() {
        return super.getSize() + ((photo == null) ? 0 : 4); // photo link
    }
}
