package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.UUID;

/**
 * A Base Entity model with default metrics
 */

public abstract class BaseEntity {

    Long id;
    UUID uuid;
    String name;
    Date creationDate;
    Date changeDate;

    protected BaseEntity() {
    }

    protected BaseEntity(Long id, UUID uuid, String name, Date creationDate, Date changeDate) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.creationDate = creationDate;
        this.changeDate = changeDate;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    /**
     * Approximate size of this entity without related entities
     *
     * @return size in bits
     */
    int getSize() {
        return 3 * Long.SIZE                                    // id, uuid
                + ((name == null) ? 0 : name.getBytes().length) // name
                + 2 * (Long.SIZE + 4);                          // dates
    }
}
