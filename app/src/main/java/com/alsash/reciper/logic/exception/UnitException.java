package com.alsash.reciper.logic.exception;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

/**
 * An logical exception caused by wrong unit
 */
public class UnitException extends RuntimeException {
    public UnitException(BaseEntity entity, String requiredUnit, String unknownValue) {
        super(entity.getClass().getName()
                + " " + entity.getId()
                + ": has unknown " + requiredUnit + ": "
                + ((unknownValue == null) ? "null" : unknownValue));
    }
}
