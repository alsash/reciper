package com.alsash.reciper.model;

import com.alsash.reciper.model.entity.Category;
import com.alsash.reciper.model.entity.Recipe;
import com.alsash.reciper.model.entity.Relations;

public class RelationManager {
    private static final RelationManager instance = new RelationManager();

    private RelationManager() {

    }

    public static RelationManager getInstance() {
        return instance;
    }

    public Relations<Category, Recipe> getRecipeRelations(Category category) {

    }

}
