package com.alsash.reciper.api.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Author entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "AUTHOR",
        active = true,
        generateConstructors = false
)
public class AuthorDb {
    @Id
    Long id;
    @Unique
    String uuid;
    @Unique
    String mail;
    String name;
    @Index(name = "photo", unique = true)
    String photoUuid;

    public AuthorDb() {
    }

}
