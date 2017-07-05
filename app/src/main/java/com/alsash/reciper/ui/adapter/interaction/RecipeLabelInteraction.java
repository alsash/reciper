package com.alsash.reciper.ui.adapter.interaction;

import com.alsash.reciper.mvp.model.entity.Label;

/**
 * Simple label entity interactions
 */
public interface RecipeLabelInteraction {

    /**
     * On long click (3D touch some time...)
     *
     * @param label entity
     */
    void onPress(Label label, int position);
}
