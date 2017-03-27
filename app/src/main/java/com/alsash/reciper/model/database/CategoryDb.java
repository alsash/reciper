package com.alsash.reciper.model.database;

import com.alsash.reciper.model.entity.Category;

public class CategoryDb implements Category {

    private long id;

    private String name;

    public CategoryDb(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
