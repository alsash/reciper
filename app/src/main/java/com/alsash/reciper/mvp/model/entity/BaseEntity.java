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
}
