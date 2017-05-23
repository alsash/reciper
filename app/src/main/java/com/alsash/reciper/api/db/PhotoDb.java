package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Photo entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "PHOTO",
        active = true,
        generateConstructors = false
)
public class PhotoDb {
    @Id
    Long id;
    @Unique
    String uuid;
    String url;
    String uri;

    public PhotoDb() {
    }

}
