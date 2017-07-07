package com.alsash.reciper.mvp.view;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

import java.util.Set;

/**
 * Simple list view
 */
public interface BaseSelectionDialogView<M extends BaseEntity> extends BaseListView<M> {

    void setSelection(boolean multi, Set<String> uuid);

    void finishView();
}
