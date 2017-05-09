package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A Label model
 */
public class Label extends BaseRecipeGroup {

    Label() {
    }

    Label(Long id,
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
        if (!(o instanceof Label)) return false;
        Label label = (Label) o;
        return uuid != null ? uuid.equals(label.uuid) : label.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
