package com.alsash.reciper.app.util;

/**
 * Simple mutable boolean object
 */
public class MutableBoolean {
    private boolean value;

    public synchronized boolean is() {
        return value;
    }

    public synchronized MutableBoolean set(boolean value) {
        this.value = value;
        return this;
    }
}
