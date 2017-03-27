package com.alsash.reciper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract entity manager
 *
 * @param <E> entity
 */
public abstract class EntityManager<E> {
    protected List<E> entities;

    protected EntityManager(int entityCount) {
        this.entities = getEmptyList();
        for (int i = 0; i < entityCount; i++) {

        }
    }

    public abstract E newEntity();

    protected List<E> getEmptyList() {
        return new ArrayList<>();
    }

    public E getEntity(long id) {

    }

    public List<E> getEntities() {
        return entities;
    }
}
