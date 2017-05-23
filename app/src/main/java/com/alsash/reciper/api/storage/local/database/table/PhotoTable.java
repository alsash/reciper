package com.alsash.reciper.api.storage.local.database.table;

import com.alsash.reciper.api.storage.local.database.converter.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.UUID;

/**
 * Local relational database table that represents the Photo entity
 */
@Entity(nameInDb = "PHOTO")
public class PhotoTable {
    @Id
    private Long id;
    @Unique
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID uuid;
    private String name;
    private Date creationDate;
    private Date changeDate;
    private String url;
    private String path;

    @Generated(hash = 788021138)
    public PhotoTable(Long id, UUID uuid, String name, Date creationDate,
                      Date changeDate, String url, String path) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.creationDate = creationDate;
        this.changeDate = changeDate;
        this.url = url;
        this.path = path;
    }

    @Generated(hash = 1809391605)
    public PhotoTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getChangeDate() {
        return this.changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
