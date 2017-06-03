package com.alsash.reciper.logic.exception;

/**
 * An logical exception caused by internet connection unavailable
 */
public class MainThreadException extends RuntimeException {
    public MainThreadException(String method) {
        super(method + " must be called on background thread");
    }
}
