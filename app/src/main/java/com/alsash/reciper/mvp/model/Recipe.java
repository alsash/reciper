package com.alsash.reciper.mvp.model;

import java.util.List;

/**
 * Created by aldolyna on 4/3/17.
 */

public interface Recipe {

    Long getId();

    String getName();

    Category getCategory();

    List<Label> getLabels();
}
