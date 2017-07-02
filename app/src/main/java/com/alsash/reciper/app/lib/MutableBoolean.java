package com.alsash.reciper.app.lib;

/**
 * Simple mutable boolean class
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
