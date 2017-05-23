package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Label entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "LABEL",
        active = true,
        generateConstructors = false
)
public class LabelDb {
    @Id
    Long id;
    @Unique
    String uuid;
    String name;

    public LabelDb() {
    }

}
