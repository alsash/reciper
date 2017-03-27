package com.alsash.reciper.model.entity;

import java.util.List;

public interface Relations<O, S> extends Relation<O, S> {
    List<S> getSubjects();
}
