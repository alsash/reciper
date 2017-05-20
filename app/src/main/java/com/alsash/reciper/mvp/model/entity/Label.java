package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.UUID;

/**
 * A Label model
 */
public class Label extends BaseEntity {

    Label() {
    }

    public Label(Long id,
                 UUID uuid,
                 String name,
                 Date creationDate,
                 Date changeDate) {
        super(id, uuid, name, creationDate, changeDate);
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

    @Override
    int getSize() {
        return super.getSize();
    }
}
