package com.alsash.reciper.mvp.view;

import com.alsash.reciper.app.lib.MutableBoolean;
import com.alsash.reciper.app.lib.MutableString;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Photo;

/**
 * A Entity list view
 */
public interface EntityListView extends BaseListView<BaseEntity> {

    void setEntityClass(Class<?> entityClass);

    void showDeleteSuccessMessage(String entityName, MutableBoolean reject);

    void showDeleteFailMessage(String entityName, int recipesCount);

    void showPhotoEditDialog(Photo photo, MutableString listener);
}
