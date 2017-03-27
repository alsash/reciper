package com.alsash.reciper.model.relation;

import android.support.annotation.Nullable;

public interface Relation<O, S> {
    O getObject();

    @Nullable
    S getSubject();
}
