package com.alsash.reciper.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Local database table that holds relations between recipes and labels
 */
@Entity(nameInDb = "RECIPE_LABEL")
public class RecipeLabelJoinDb {
    @Id
    private long id;
    private long recipeId;
    private long labelId;
// Next will be generated sources
@Generated(hash = 1310113035)
public RecipeLabelJoinDb(long id, long recipeId, long labelId) {
    this.id = id;
    this.recipeId = recipeId;
    this.labelId = labelId;
}

    @Generated(hash = 209855711)
    public RecipeLabelJoinDb() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }
}
