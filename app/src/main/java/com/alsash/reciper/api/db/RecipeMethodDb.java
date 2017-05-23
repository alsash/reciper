package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Method entity,
 * has a to-one relation with the Recipe entity and its methods,
 * persists in local relational database table with the help of GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE_METHOD",
        active = true,
        generateConstructors = false
)
public class RecipeMethodDb {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "recipe")
    String recipeUuid;
    @NotNull
    long index;
    String body;

    public RecipeMethodDb() {
    }
}
