package com.alsash.reciper.database;

import org.greenrobot.greendao.annotation.Entity;
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
}
