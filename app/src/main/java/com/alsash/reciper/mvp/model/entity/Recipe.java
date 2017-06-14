package com.alsash.reciper.mvp.model.entity;

import java.util.Date;

/**
 * A Recipe entity
 */
public interface Recipe extends BaseEntity {

    Date getCreatedAt();

    String getName();

    String getSource();

    String getDescription();

    int getServings();

    double getMassFlowRateGps(); // gram per second

    boolean isFavorite();

    Photo getMainPhoto();

    Author getAuthor();

    Category getCategory();
}
