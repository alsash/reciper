package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

/**
 * A Entity list view
 */
public interface EntityListView extends BaseListView<BaseEntity> {

    void setEntityClass(Class<?> entityClass);
}
