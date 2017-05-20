package com.alsash.reciper.mvp.model.entity;

import java.util.Date;
import java.util.UUID;


/**
 * A Photo model
 */
public class Photo extends BaseEntity {

    String url;
    String path;

    Photo() {
    }

    Photo(Long id,
          UUID uuid,
          String name,
          Date creationDate,
          Date changeDate,
          String url,
          String path) {
        super(id, uuid, name, creationDate, changeDate);
        this.url = url;
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;
        Photo photo = (Photo) o;
        return uuid != null ? uuid.equals(photo.uuid) : photo.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    int getSize() {
        return super.getSize()
                + ((url == null) ? 0 : url.getBytes().length)
                + ((path == null) ? 0 : path.getBytes().length);
    }
}
