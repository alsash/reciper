package com.alsash.reciper.logic.restriction;

/**
 * A definition of any entity
 */
public class EntityRestriction {
    public String entityUuid;
    public Class<?> entityClass;

    public EntityRestriction(String entityUuid, Class<?> entityClass) {
        this.entityUuid = entityUuid;
        this.entityClass = entityClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityRestriction)) return false;
        EntityRestriction that = (EntityRestriction) o;
        if (entityUuid != null ? !entityUuid.equals(that.entityUuid) : that.entityUuid != null)
            return false;
        return entityClass != null ? entityClass.equals(that.entityClass)
                : that.entityClass == null;
    }

    @Override
    public int hashCode() {
        int result = entityUuid != null ? entityUuid.hashCode() : 0;
        result = 31 * result + (entityClass != null ? entityClass.hashCode() : 0);
        return result;
    }
}
