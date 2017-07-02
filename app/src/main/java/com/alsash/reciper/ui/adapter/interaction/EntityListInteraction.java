package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

/**
 * A BaseEntity list interaction
 */

public interface EntityListInteraction {

    void onOpen(BaseEntity entity);

    void onEditValues(BaseEntity entity, String... values);

    void onEditPhoto(BaseEntity entity);

}
