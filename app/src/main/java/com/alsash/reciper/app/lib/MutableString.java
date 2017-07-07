package com.alsash.reciper.app.lib;

/**
 * Simple mutable String class
 */
public class MutableString {
    protected String value = "";

    public MutableString() {
    }

    public MutableString(String value) {
        if (value != null) this.value = value;
    }

    public synchronized String get() {
        return value;
    }

    public synchronized MutableString set(String value) {
        if (value != null) this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutableString)) return false;
        MutableString that = (MutableString) o;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
