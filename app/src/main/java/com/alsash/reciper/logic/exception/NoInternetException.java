package com.alsash.reciper.logic.exception;

/**
 * An logical exception caused by internet connection unavailable
 */
public class NoInternetException extends RuntimeException {
    public NoInternetException() {
        super("internet connection unavailable");
    }
}
