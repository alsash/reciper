package com.alsash.reciper.app.lib;

/**
 * Simple mutable boolean class
 */
public class MutableBoolean {
    protected boolean value;

    public synchronized boolean is() {
        return value;
    }

    public synchronized MutableBoolean set(boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableBoolean)) return false;
        MutableBoolean that = (MutableBoolean) o;
        return value == that.value;

    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }
}
