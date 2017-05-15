package com.alsash.reciper.mvp.model.entity;

import android.graphics.Bitmap;

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


}
