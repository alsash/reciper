package com.alsash.reciper.app.lib;

/**
 * Simple mutable Long class
 */
public class MutableLong {
    protected long value = 0;

    public MutableLong() {
    }

    public MutableLong(long value) {
        this.value = value;
    }

    public synchronized long get() {
        return value;
    }

    public synchronized MutableLong set(long value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableLong)) return false;
        MutableLong that = (MutableLong) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
