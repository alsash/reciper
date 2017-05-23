package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Category entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "CATEGORY",
        active = true,
        generateConstructors = false
)
public class CategoryDb {
    @Id
    Long id;
    @Unique
    String uuid;
    String name;
    @Index(name = "photo", unique = true)
    String photoUuid;

    public CategoryDb() {
    }

}
