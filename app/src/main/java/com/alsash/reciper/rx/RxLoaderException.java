package com.alsash.reciper.rx;

/**
 * A Exception for RxLoader and RxLoaderBuilder
 */
public class RxLoaderException extends RuntimeException {

    public RxLoaderException(String message) {
        super(message);
    }

    public RxLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
