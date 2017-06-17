package com.alsash.reciper.mvp.model.restriction;

/**
 * Created by alsash on 6/17/17.
 */

public class EntityRestriction {
    public String entityUuid;
    public Class<?> entityClass;

    public EntityRestriction(String entityUuid, Class<?> entityClass) {
        this.entityUuid = entityUuid;
        this.entityClass = entityClass;
    }
}
