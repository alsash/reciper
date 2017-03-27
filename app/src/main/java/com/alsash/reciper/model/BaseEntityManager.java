package com.alsash.reciper.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract entity manager
 *
 * @param <E> entity
 */
public abstract class BaseEntityManager<E, K> {
    protected List<E> entities;

    protected BaseEntityManager(int entityCount) {
        this.entities = getEmptyList();
        for (int i = 0; i < entityCount; i++) {

        }
    }

    public E create() {
        E entity = newEntity();
        entities.add(entity);
        return entity;
    }

    @Nullable
    public E search(K key) {
        if (key == null) return null;
        for (E entity : entities) {
            if (key.equals(getKey(entity))) return entity;
        }
        return null;
    }

    public List<E> list() {
        return entities;
    }

    protected abstract K getKey(E entity);

    protected abstract E newEntity();

    protected List<E> getEmptyList() {
        return new ArrayList<>();
    }
}
