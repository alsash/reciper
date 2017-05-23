package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A to-many relation model of the Recipe and the Photo entities,
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE_PHOTO",
        active = true,
        generateConstructors = false
)
public class RecipePhotoDb {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "recipe")
    String recipeUuid;
    @Index(name = "photo")
    String photoUuid;

    public RecipePhotoDb() {
    }
}
