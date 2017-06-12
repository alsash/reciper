package com.alsash.reciper.mvp.model.entity;

import java.util.Date;

/**
 * A base entity interface
 */
public interface BaseEntity {

    Long getId();

    String getUuid();

    Date getChangedAt();
}
