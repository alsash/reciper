package com.alsash.reciper.app.lib;

/**
 * Simple mutable String class
 */
public class MutableString {
    private String value = "";

    public synchronized String get() {
        return value;
    }

    public synchronized MutableString set(String value) {
        if (value != null) this.value = value;
        return this;
    }
}
