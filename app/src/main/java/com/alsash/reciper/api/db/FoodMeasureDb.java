package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A to-many relation model of the Food entity and its custom measures,
 * persists in local relational database table with the help of GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "FOOD_MEASURE",
        active = true,
        generateConstructors = false
)
public class FoodMeasureDb {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "food", unique = true)
    String foodUuid;
    String unit;
    @NotNull
    double weight;
    String weightUnit;

    public FoodMeasureDb() {
    }
}
