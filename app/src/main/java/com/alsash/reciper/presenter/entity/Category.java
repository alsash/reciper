package com.alsash.reciper.presenter.entity;

import java.util.List;

public interface Category extends BaseEntity {

    List<Recipe> getRecipes();
}
