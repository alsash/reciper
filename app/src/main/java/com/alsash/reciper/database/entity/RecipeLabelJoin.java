package com.alsash.reciper.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Local database table that holds relations between recipes and labels
 */
@Entity
public class RecipeLabelJoin {

    @Id
    private Long id;

    private Long recipeId;

    private Long labelId;

    // Next will be generated sources

    @Generated(hash = 1589061011)
    public RecipeLabelJoin(Long id, Long recipeId, Long labelId) {
        this.id = id;
        this.recipeId = recipeId;
        this.labelId = labelId;
    }

    @Generated(hash = 627404047)
    public RecipeLabelJoin() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

// Next will be generated sources
}
