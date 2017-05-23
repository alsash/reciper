package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * A model of the Recipe entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE",
        active = true,
        generateConstructors = false
)
public class RecipeDb {
    @Id
    Long id;
    @Unique
    String uuid;
    String name;
    @Index(name = "date DESC")
    Date date;
    String source;
    String description;
    @NotNull
    boolean bookmark;
    @NotNull
    int servings;
    @NotNull
    double massFlowRate;
    @Index(name = "category")
    String categoryUuid;
    @Index(name = "author")
    String authorUuid;

    public RecipeDb() {
    }

}
