package com.alsash.reciper.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Local database table that holds recipes
 */
@Entity(nameInDb = "RECIPE",
        indexes = {
                @Index(value = "name", unique = true)
        })
public class RecipeDb {
    @Id
    private long id;
    private String name;
    private long categoryId;
    @ToOne(joinProperty = "categoryId")
    private CategoryDb category;
    @ToMany
    @JoinEntity(
            entity = RecipeLabelJoinDb.class,
            sourceProperty = "recipeId",
            targetProperty = "labelId"
    )
    private List<LabelDb> labels;
// Next will be generated sources
}
