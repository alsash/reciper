package com.alsash.reciper.mvp.model.entity;

/**
 * An Author entity
 */
public interface Author extends BaseEntity {

    String getName();

    String getMail();

    Photo getPhoto();
}
