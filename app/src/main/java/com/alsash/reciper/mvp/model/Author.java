package com.alsash.reciper.mvp.model;

/**
 * An Author entity
 */
public interface Author extends Entity {

    String getName();

    String getMail();

    Photo getPhoto();
}
