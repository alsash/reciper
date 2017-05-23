package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Ingredient entity
 * represented by a to-many relation model of the Recipe and the Food entities,
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE_FOOD",
        active = true,
        generateConstructors = false
)
public class RecipeFoodDb {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "recipe")
    String recipeUuid;
    @Index(name = "food")
    String foodUuid;
    String name;
    double weight;
    String weightUnit;

    public RecipeFoodDb() {
    }
}
