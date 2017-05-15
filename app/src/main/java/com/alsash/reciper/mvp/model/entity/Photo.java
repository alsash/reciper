package com.alsash.reciper.mvp.model.entity;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * A Photo model
 */
public class Photo extends BaseEntity {

    String url;
    String path;
    Bitmap data;

    Photo() {
    }

    Photo(Long id,
          UUID uuid,
          String name,
          Date creationDate,
          Date changeDate,
          String url,
          String path,
          Bitmap data) {
        super(id, uuid, name, creationDate, changeDate);
        this.url = url;
        this.path = path;
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    @Nullable
    public Bitmap getData() {
        return data;
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
                + ((path == null) ? 0 : path.getBytes().length)
                + ((data == null) ? 0 : data.getByteCount());
    }
}
