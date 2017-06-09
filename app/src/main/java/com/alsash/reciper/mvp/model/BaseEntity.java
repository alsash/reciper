package com.alsash.reciper.mvp.model;

import java.util.Date;

/**
 * A base entity
 */
public interface BaseEntity {

    Long getId();

    String getUuid();

    Date getChangedAt();
}
