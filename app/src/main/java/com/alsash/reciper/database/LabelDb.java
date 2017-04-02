package com.alsash.reciper.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Local database table that holds labels of recipes
 */
@Entity(nameInDb = "label",
        indexes = {
                @Index(value = "name", unique = true)
        })
public class LabelDb {
    @Id
    private long id;
    private String name;
    @ToMany
    @JoinEntity(
            entity = RecipeLabelJoinDb.class,
            sourceProperty = "labelId",
            targetProperty = "recipeId"
    )
    private List<RecipeDb> recipes;
// Next will be generated sources
}
