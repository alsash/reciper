package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

/**
 * Simple entity interaction
 */
public interface EntitySelectionInteraction {

    void onSelect(BaseEntity entity);
}
