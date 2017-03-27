package com.alsash.reciper.model.entity;

import java.util.List;

public interface Relations extends Relation {
    List<Long> getChildIds();
}
