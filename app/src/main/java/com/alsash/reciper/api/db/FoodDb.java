package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Food entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "FOOD",
        active = true,
        generateConstructors = false
)
public class FoodDb {
    @Id
    Long id;
    @Unique
    String uuid;
    Long usdaNdbno;
    String name;
    @NotNull
    double protein;
    @NotNull
    double fat;
    @NotNull
    double carbs;
    @NotNull
    double weightUnit;
    @NotNull
    double energy;
    @NotNull
    double energyEnit;

    public FoodDb() {
    }

}
