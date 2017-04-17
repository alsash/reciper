package com.alsash.reciper.mvp.model.entity.database;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe class that represents Recipe MVP interface with
 * delegation to Recipe entity stored in database
 */
public class RecipeMvpDb implements Recipe {

    private final com.alsash.reciper.database.entity.Recipe recipeDb;

    public RecipeMvpDb(com.alsash.reciper.database.entity.Recipe recipeDb) {
        this.recipeDb = recipeDb;
    }

    @Override
    public Long getId() {
        return recipeDb.getId();
    }

    @Override
    public String getName() {
        return recipeDb.getName();
    }

    @Override
    public Category getCategory() {
        return new CategoryMvpDb(recipeDb.getCategory());
    }

    @Override
    public List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        for (com.alsash.reciper.database.entity.Label label : recipeDb.getLabels()) {
            labels.add(new LabelMvpDb(label));
        }
        return labels;
    }
}
