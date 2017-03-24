package com.alsash.reciper.model.models;

import java.util.List;

public interface Category {
    long getId();

    String getName();
    
    List<Long> getRecipeIds();
}
