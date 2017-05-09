package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A Recipe model
 */
public class Recipe extends BaseEntity {

    Category category;
    List<Label> labels;

    Recipe() {
    }

    Recipe(Long id,
           UUID uuid,
           String name,
           Date creationDate,
           Date changeDate,
           Category category,
           List<Label> labels) {
        super(id, uuid, name, creationDate, changeDate);
        this.category = category;
        this.labels = labels;
    }

    public Category getCategory() {
        return category;
    }

    public List<Label> getLabels() {
        return labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return uuid != null ? uuid.equals(recipe.uuid) : recipe.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
