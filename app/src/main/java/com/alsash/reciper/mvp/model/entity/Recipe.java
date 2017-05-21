package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A Recipe model
 */
public class Recipe extends BaseEntity {

    boolean bookmarked;
    Category category;
    List<Label> labels;
    Photo photo;

    Recipe() {
    }

    Recipe(Long id,
           UUID uuid,
           String name,
           Date creationDate,
           Date changeDate,
           Category category,
           List<Label> labels,
           Photo photo) {
        super(id, uuid, name, creationDate, changeDate);
        this.category = category;
        this.labels = labels;
        this.photo = photo;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public Category getCategory() {
        return category;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public Photo getPhoto() {
        return photo;
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

    @Override
    int getSize() {
        return super.getSize()
                + ((category == null) ? 0 : 4)
                + ((labels == null) ? 0 : 4)
                + ((photo == null) ? 0 : 4);
    }
}
