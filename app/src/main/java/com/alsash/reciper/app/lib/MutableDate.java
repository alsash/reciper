package com.alsash.reciper.app.lib;

import java.util.Date;

/**
 * Simple mutable Date class
 */
public class MutableDate {
    protected Date value;

    public MutableDate() {
        this(new Date());
    }

    public MutableDate(Date date) {
        value = date != null ? date : new Date();
    }

    public synchronized Date get() {
        return value;
    }

    public synchronized MutableDate set(Date value) {
        if (value != null) this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableDate)) return false;
        MutableDate that = (MutableDate) o;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
